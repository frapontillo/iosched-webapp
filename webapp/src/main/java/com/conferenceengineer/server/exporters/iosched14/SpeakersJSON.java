package com.conferenceengineer.server.exporters.iosched14;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.LastModification;
import com.conferenceengineer.server.datamodel.Presenter;
import com.conferenceengineer.server.exporters.AbstractIOSched14ManifestSubfile;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.EntityManager;

public final class SpeakersJSON extends AbstractIOSched14ManifestSubfile {

    private static final String EXPORT_NAME = "speakers";
    private static final String[] DATA_DEPENDENCIES = {LastModification.NAME_SPEAKERS};

    public SpeakersJSON(final EntityManager em, final Conference conference) {
        super(em, conference);
    }

    public void addToJSONObject(final JSONObject root) {
        JSONArray presenters = new JSONArray();

        for(Presenter presenter : mConference.getPresenterList()) {
            JSONObject json = new JSONObject();
            json.put("id", Integer.toString(presenter.getId()));
            json.put("name", presenter.getName());
            json.put("bio", presenter.getShortBiography());
            // TODO: company
            json.put("thumbnailUrl", presenter.getImageURL());
            // TODO: plusoneUrl
            presenters.put(json);
        }

        root.put("speakers", presenters);
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
