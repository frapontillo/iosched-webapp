package com.conferenceengineer.server.exporters.iosched14;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.LastModification;
import com.conferenceengineer.server.datamodel.Track;
import com.conferenceengineer.server.exporters.AbstractIOSched14ManifestSubfile;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.EntityManager;

public final class TagsJSON extends AbstractIOSched14ManifestSubfile {

    private static final String EXPORT_NAME = "tags";
    private static final String[] DATA_DEPENDENCIES = {LastModification.NAME_TRACKS};

    static final String CATEGORY_THEME = "THEME";

    public TagsJSON(final EntityManager em, final Conference conference) {
        super(em, conference);
    }

    public void addToJSONObject(final JSONObject root) {
        JSONArray tags = new JSONArray();

        addCategoryTags(tags);

        root.put("tags", tags);
    }

    private void addCategoryTags(final JSONArray tags) {
        int orderCounter = 1;
        for(Track track: mConference.getTrackList()) {
            JSONObject tagJSON = new JSONObject();
            tagJSON.put("category", CATEGORY_THEME);
            tagJSON.put("tag", CATEGORY_THEME+"_"+track.getId());
            tagJSON.put("name", track.getName());
            tagJSON.put("abstract", track.getDescription());
            tagJSON.put("order_in_category", orderCounter++);
            tags.put(tagJSON);
        }
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
