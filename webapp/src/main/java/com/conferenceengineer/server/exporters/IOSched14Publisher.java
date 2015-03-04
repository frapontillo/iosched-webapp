package com.conferenceengineer.server.exporters;

import com.conferenceengineer.server.cdn.ContentDeliveryNetwork;
import com.conferenceengineer.server.cdn.ContentDeliveryNetworkFactory;
import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.LastModification;
import com.conferenceengineer.server.datamodel.LastModificationDAO;
import com.conferenceengineer.server.datamodel.utils.EntityManagerFactoryWrapper;
import com.conferenceengineer.server.exporters.iosched14.BlocksJSON;
import com.conferenceengineer.server.exporters.iosched14.ExpertsJSON;
import com.conferenceengineer.server.exporters.iosched14.HashtagsJSON;
import com.conferenceengineer.server.exporters.iosched14.PartnersJSON;
import com.conferenceengineer.server.exporters.iosched14.RoomsJSON;
import com.conferenceengineer.server.exporters.iosched14.SessionsJSON;
import com.conferenceengineer.server.exporters.iosched14.SpeakersJSON;
import com.conferenceengineer.server.exporters.iosched14.TagsJSON;
import com.conferenceengineer.server.exporters.iosched14.VideosJSON;
import com.conferenceengineer.server.utils.CloudStoreCredentials;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Publisher which pushes the conference data to the Rackspace endpoint for this conference
 */
public class IOSched14Publisher extends ExportPublisher {

    private static final String MANIFEST_FILENAME = "manifest.json";
    private static final String MANIFEST_FORMAT_IDENTIFIER = "iosched-json-v1";

    private static final String UPDATES_CONTAINER = "updates.conferenceengineer.com";

    private static final Class[] EXPORTERS = {
            BlocksJSON.class,
            ExpertsJSON.class,
            HashtagsJSON.class,
            PartnersJSON.class,
            RoomsJSON.class,
            SessionsJSON.class,
            SpeakersJSON.class,
            TagsJSON.class,
            VideosJSON.class
    };

    private Conference mConference;
    private CloudStoreCredentials mCloudStoreCredentials;
    private List<String> mExportFilenames = new ArrayList<>();

    public IOSched14Publisher(final Conference conference) {
        mConference = conference;
        mCloudStoreCredentials = CloudStoreCredentials.getInstance();
    }

    public void run() {
        EntityManager em = EntityManagerFactoryWrapper.getInstance().getEntityManager();
        try(ContentDeliveryNetwork cdn = ContentDeliveryNetworkFactory.getInstance(UPDATES_CONTAINER)) {
            exportChangedData(em, cdn);
        } finally {
            em.close();
        }
    }

    private void exportChangedData(final EntityManager em, final ContentDeliveryNetwork cdn) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        for (Class<? extends AbstractIOSched14ManifestSubfile> exporter : EXPORTERS) {
            try {
                runExporter(em, cdn, exporter);
            } catch (IOException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Problem during export", e);
                setLastIOException(e);
                transaction.rollback();
                return;
            }
        }

        if(cdn.hasDataBeenSent()) {
            String manifest = createManifest();
            try {
                String filename = getFullPathForFile(MANIFEST_FILENAME);
                cdn.putData(filename, manifest);
            } catch (IOException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Problem publishing manifest", e);
                setLastIOException(e);
                transaction.rollback();
                return;
            }
        }

        ensureAllTrackedEntitiesHaveALastModifiedTime(em);

        transaction.commit();
        setComplete();
    }

    private void runExporter(final EntityManager em, final ContentDeliveryNetwork cdn,
                            final Class<? extends AbstractIOSched14ManifestSubfile> exporterClass)
            throws IOException {
        try {
            Constructor<? extends AbstractIOSched14ManifestSubfile> exporterConstructor =
                    exporterClass.getConstructor(EntityManager.class, Conference.class);
            AbstractIOSched14ManifestSubfile exporter =
                    exporterConstructor.newInstance(em, mConference);

            boolean isNewExportNeeded =
                    !cdn.hasObjectCalled(getFullPathForFile(exporter.getFilename())) ||
                    exporter.hasDependantDataBeenModifiedSinceLastExport();
            if(isNewExportNeeded) {
                exporter.increaseExportSerialNumber();
            }

            String filename = exporter.getFilename();
            mExportFilenames.add(filename);

            if(isNewExportNeeded) {
                String fullFilename = getFullPathForFile(filename);
                cdn.putData(fullFilename, exporter.toString());
                exporter.recordExport();
            }
        } catch(NoSuchMethodException | IllegalAccessException
                | InvocationTargetException | InstantiationException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Problem pushing update.", e);
            throw new IOException(e);
        }
    }

    private void ensureAllTrackedEntitiesHaveALastModifiedTime(final EntityManager entityManager) {
        LastModificationDAO lmDAO = LastModificationDAO.getInstance();
        for(String entityName : LastModification.TRACKED_ENTITY_NAMES) {
            if(lmDAO.getByName(entityManager, mConference, entityName) == null) {
                lmDAO.recordUpdate(entityManager, mConference, entityName);
            }
        }
    }

    private String getFullPathForFile(final String filename) {
        return mCloudStoreCredentials.getContainerPrefix()+"events/"+mConference.getId()+"/iosched14/"+filename;
    }

    public String getManifestUrl() {
        return "http://"+UPDATES_CONTAINER+"/"+getFullPathForFile(MANIFEST_FILENAME);
    }

    private String createManifest() {
        JSONObject root = new JSONObject();
        root.put("format", MANIFEST_FORMAT_IDENTIFIER);
        JSONArray files = new JSONArray();
        for(String filename : mExportFilenames) {
            files.put(filename);
        }
        root.put("data_files", files);
        return root.toString();
    }


}
