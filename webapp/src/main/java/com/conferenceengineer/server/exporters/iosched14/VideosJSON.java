package com.conferenceengineer.server.exporters.iosched14;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.exporters.AbstractIOSched14ManifestSubfile;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.EntityManager;

/**
 * Exporter for speakers in the JSON format iosched wants
 */
public final class VideosJSON extends AbstractIOSched14ManifestSubfile {

    private static final String EXPORT_NAME = "videos";
    private static final String[] DATA_DEPENDENCIES = {};

    public VideosJSON(final EntityManager em, final Conference conference) {
        super(em, conference);
    }

    public void addToJSONObject(final JSONObject root) {
        JSONArray videos = new JSONArray();
        /*
{
    "year": "2013",
    "title": "What's New in Android Developer Tools",
    "desc": "A summary of new features for Android developers",
    "vid": "lmv1dTnhLH4",
    "id": "lmv1dTnhLH4",
    "thumbnailUrl": "http://img.youtube.com/vi/lmv1dTnhLH4/hqdefault.jpg",
    "topic": "Android",
    "speakers": "Xavier Ducrohet, Tor Norbye"
}         */

        root.put("videos", videos);
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
