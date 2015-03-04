package com.conferenceengineer.server.exporters.iosched14;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.LastModification;
import com.conferenceengineer.server.datamodel.Presenter;
import com.conferenceengineer.server.exporters.AbstractIOSched14Exporter;
import com.conferenceengineer.server.exporters.AbstractIOSched14ManifestSubfile;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.EntityManager;

/**
 * Exporter for speakers in the JSON format iosched wants
 */
public final class ExpertsJSON extends AbstractIOSched14ManifestSubfile {

    private static final String EXPORT_NAME = "experts";

    private static final String[] DATA_DEPENDENCIES =
            {LastModification.NAME_SPEAKERS};

    public ExpertsJSON(final EntityManager em, Conference conference) {
        super(em, conference);
    }

    public void addToJSONObject(final JSONObject root) {
        JSONArray presenters = new JSONArray();

        for(Presenter presenter : mConference.getPresenterList()) {
            JSONObject json = new JSONObject();
            json.put("id", Integer.toString(presenter.getId()));
            json.put("name", presenter.getName());
            json.put("attending", Boolean.TRUE);
            json.put("bio", presenter.getShortBiography());
            // TODO: city
            // TODO: country
            json.put("imageUrl", presenter.getImageURL());
            // TODO: plusId
            json.put("url", presenter.getSocialLink());
            presenters.put(json);
        }

        root.put("experts", presenters);
    }

    @Override
    protected String getExportName() {
        return EXPORT_NAME;
    }

    @Override
    protected String[] getDependantDataNames() {
        return DATA_DEPENDENCIES;
    }
}
