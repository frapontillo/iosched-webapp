package com.conferenceengineer.server.exporters.iosched13;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.Presenter;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Exporter for speakers in the JSON format iosched wants
 */
public final class SpeakersJSON {

    private SpeakersJSON() {
        super();
    }

    /**
     * Create the export and place it in a string.
     */

    public static String export(final Conference conference) {
        JSONObject root = new JSONObject();
        JSONArray presenters = new JSONArray();

        for(Presenter presenter : conference.getPresenterList()) {
            JSONObject json = new JSONObject();
            json.put("id", Integer.toString(presenter.getId()));
            json.put("name", presenter.getName());
            json.put("bio", presenter.getShortBiography());
            json.put("publicPlusId", presenter.getSocialLink());
            json.put("plusoneUrl", "");
            json.put("thumbnailUrl", presenter.getImageURL());
            presenters.put(json);
        }

        root.put("presenters", presenters);
        return root.toString();
    }
}
