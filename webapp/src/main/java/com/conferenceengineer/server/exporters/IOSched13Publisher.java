package com.conferenceengineer.server.exporters;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.exporters.iosched13.SessionsJSON;
import com.conferenceengineer.server.exporters.iosched13.SpeakersJSON;
import com.conferenceengineer.server.exporters.iosched13.TrackSessionsJSON;
import com.conferenceengineer.server.utils.CloudStoreCredentials;
import org.jclouds.ContextBuilder;
import org.jclouds.io.Payload;
import org.jclouds.io.Payloads;
import org.jclouds.openstack.swift.v1.features.ObjectApi;
import org.jclouds.rackspace.cloudfiles.v1.CloudFilesApi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Publisher which pushes the conference data to the Rackspace container for this conference
 * in the iosched13 format.
 */
public class IOSched13Publisher extends ExportPublisher {

    private static final String CONTAINER_NAME = "updates.conferenceengineer.com";

    private Conference mConference;

    public IOSched13Publisher(final Conference conference) {
        mConference = conference;
    }

    public void run() {
        try {
            publishUpdate(mConference, "presenters", SpeakersJSON.export(mConference));
            publishUpdate(mConference, "tracks", TrackSessionsJSON.export(mConference));
            publishUpdate(mConference, "sessions", SessionsJSON.export(mConference));
            setComplete();
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Unable to complete export", e);
            setLastIOException(e);
        }
    }

    /**
     * Publish the data for clients to download.
     *
     * @param conference The conference the data is being exported for.
     * @param name The name of the file being exported.
     * @param data The data to be exported.
     *
     * @throws java.io.IOException
     */
    private void publishUpdate(final Conference conference, final String name, final String data)
            throws IOException {
        String objectName = "events/"+conference.getId()+"/"+name;

        ByteArrayInputStream dataStream = new ByteArrayInputStream(data.getBytes("UTF-8"));
        Payload payload = Payloads.newInputStreamPayload(dataStream);

        CloudStoreCredentials credentials = CloudStoreCredentials.getInstance();
        CloudFilesApi cloudFilesApi =
                ContextBuilder.newBuilder(credentials.getProvider())
                        .credentials(credentials.getUsername(), credentials.getAPIKey())
                        .buildApi(CloudFilesApi.class);

        ObjectApi objectApi =
                cloudFilesApi.
                        getObjectApiForRegionAndContainer(
                                credentials.getRegion(),
                                credentials.getContainerPrefix()+CONTAINER_NAME
                        );
        objectApi.put(objectName, payload);
    }

}
