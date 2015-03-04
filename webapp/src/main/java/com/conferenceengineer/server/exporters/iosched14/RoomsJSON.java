package com.conferenceengineer.server.exporters.iosched14;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.LastModification;
import com.conferenceengineer.server.datamodel.TalkLocation;
import com.conferenceengineer.server.exporters.AbstractIOSched14ManifestSubfile;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.EntityManager;

public final class RoomsJSON extends AbstractIOSched14ManifestSubfile {

    private static final String EXPORT_NAME = "rooms";
    private static final String[] DATA_DEPENDENCIES = {LastModification.NAME_LOCATIONS};

    public RoomsJSON(final EntityManager em, final Conference conference) {
        super(em, conference);
    }

    public void addToJSONObject(final JSONObject root) {
        JSONArray locations = new JSONArray();

        for(TalkLocation location : mConference.getTalkLocationList()) {
            JSONObject trackJSON = new JSONObject();
            trackJSON.put("id", "ROOM"+location.getId());
            trackJSON.put("name", location.getName());
            locations.put(trackJSON);
        }

        root.put("rooms", locations);
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
