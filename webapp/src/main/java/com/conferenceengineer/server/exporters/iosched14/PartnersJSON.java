package com.conferenceengineer.server.exporters.iosched14;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.exporters.AbstractIOSched14ManifestSubfile;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.EntityManager;

/**
 * Exporter for speakers in the JSON format iosched wants
 */
public final class PartnersJSON extends AbstractIOSched14ManifestSubfile {

    private static final String EXPORT_NAME = "partners";
    private static final String[] DATA_DEPENDENCIES = {};

    public PartnersJSON(final EntityManager em, Conference conference) {
        super(em, conference);
    }

    public void addToJSONObject(final JSONObject root) {
        JSONArray partners = new JSONArray();

        /*
{
    "id": "PARTNER123",
    "name": "Example Corp",
    "website": "http://www.example.com/",
    "logo": "http://.../example.png",
    "desc": "Example Corp produces great example material."
}
         */

        root.put("partners", partners);
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
