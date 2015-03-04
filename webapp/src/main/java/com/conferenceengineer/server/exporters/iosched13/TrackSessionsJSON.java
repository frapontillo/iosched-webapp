package com.conferenceengineer.server.exporters.iosched13;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.Talk;
import com.conferenceengineer.server.datamodel.Track;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Exporter for talk slots in the JSON format iosched wants
 */
public final class TrackSessionsJSON {

    private TrackSessionsJSON() {
        super();
    }

    /**
     * Create the export and place it in a string.
     */

    public static String export(final Conference conference) {
        JSONObject root = new JSONObject();
        JSONArray tracks = new JSONArray();

        for(Track track : conference.getTrackList()) {
            JSONObject json = new JSONObject();
            json.put("id", Integer.toString(track.getId()));
            json.put("title", track.getName());
            json.put("description", track.getDescription());

            JSONArray sessions = new JSONArray();
            for(Talk talk : track.getTalkList()) {
                sessions.put(Integer.toString(talk.getId()));
            }
            json.put("sessions", sessions);

            tracks.put(json);
        }

        root.put("tracks", tracks);
        return root.toString();
    }
}
