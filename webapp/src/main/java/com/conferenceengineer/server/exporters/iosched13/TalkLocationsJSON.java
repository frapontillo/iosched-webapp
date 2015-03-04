package com.conferenceengineer.server.exporters.iosched13;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.TalkLocation;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Exporter for talk slots in the JSON format iosched wants
 */
public final class TalkLocationsJSON {

    private TalkLocationsJSON() {
        super();
    }

    /**
     * Create the export and place it in a string.
     */

    public static String export(final Conference conference) {
        JSONObject root = new JSONObject();
        JSONArray locations = new JSONArray();

        for(TalkLocation location : conference.getTalkLocationList()) {
            JSONObject trackJSON = new JSONObject();
            trackJSON.put("id", Integer.toString(location.getId()));
            trackJSON.put("name", location.getName());
            trackJSON.put("floor", location.getAddress());
            locations.put(trackJSON);
        }

        root.put("rooms", locations);
        return root.toString();
    }
}
