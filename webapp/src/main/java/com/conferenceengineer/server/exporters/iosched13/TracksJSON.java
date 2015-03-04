package com.conferenceengineer.server.exporters.iosched13;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.Talk;
import com.conferenceengineer.server.datamodel.Track;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Exporter for Track data
 */
public final class TracksJSON {

    private TracksJSON() {
        super();
    }

    /**
     * Create the export and place it in a string.
     */

    public static String export(final Conference conference) {
        JSONObject root = new JSONObject();
        JSONArray tracks = new JSONArray();

        for(Track track : conference.getTrackList()) {
            JSONObject trackJSON = new JSONObject();
            trackJSON.put("id", Integer.toString(track.getId()));
            trackJSON.put("title", track.getName());
            trackJSON.put("description", track.getDescription());
            String colour = track.getColour();
            if(colour == null) {
                colour = "#336699";
            }
            trackJSON.put("colour", colour);
            trackJSON.put("level", "1");
            trackJSON.put("meta", 1);   // Sessions only

            JSONArray sessions = new JSONArray();
            for(Talk talk : track.getTalkList()) {
                sessions.put(Integer.toString(talk.getId()));
            }
            trackJSON.put("sessions", sessions);

            tracks.put(trackJSON);
        }

        root.put("tracks", tracks);
        return root.toString();
    }
}
