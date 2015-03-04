package com.conferenceengineer.server.exporters;

import com.conferenceengineer.server.datamodel.Conference;
import org.json.JSONObject;

/**
 * Base class for classes which can provide a String which holds a JSON representation of
 * some data in the format expected by the modified iosched14 application.
 */
public abstract class AbstractIOSched14Exporter {

    protected Conference mConference;

    public AbstractIOSched14Exporter(final Conference conference) {
        mConference = conference;
    }

    public abstract void addToJSONObject(JSONObject object);

    public String toString(final Conference conference) {
        JSONObject root = new JSONObject();
        addToJSONObject(root);
        return root.toString();
    }

    public String toString() {
        return toString(mConference);
    }
}
