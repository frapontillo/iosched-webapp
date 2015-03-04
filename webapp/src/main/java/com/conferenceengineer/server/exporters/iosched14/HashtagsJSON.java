package com.conferenceengineer.server.exporters.iosched14;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.ConferenceDay;
import com.conferenceengineer.server.datamodel.LastModification;
import com.conferenceengineer.server.datamodel.Talk;
import com.conferenceengineer.server.datamodel.TalkSlot;
import com.conferenceengineer.server.exporters.AbstractIOSched14ManifestSubfile;
import com.conferenceengineer.server.utils.TagUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

/**
 * Exporter for talk slots in the JSON format iosched wants
 */
public final class HashtagsJSON extends AbstractIOSched14ManifestSubfile {

    private static final String EXPORT_NAME = "hashtags";

    private static final String[] DATA_DEPENDENCIES =
            {LastModification.NAME_HASHTAGS};

    private static final String[] COLOUR_PALETTE = {
            "#b0120a", "#311b92", "#01579b", "#0d5302", "#e65100", "#212121",
            "#880e4f", "#1a237e", "#006064", "#33691e", "#bf360c", "#263238",
            "#4a148c", "#2a36b1", "#004d40", "#827717", "#3e2723",
    };

    private static final int COLOUR_PALETTE_LENGTH = COLOUR_PALETTE.length;


    private static final int MAXIMUM_TAG_COUNT = 20;

    public HashtagsJSON(final EntityManager em, final Conference conference) {
        super(em, conference);
    }

    public void addToJSONObject(final JSONObject root) {
        JSONArray hashtags = new JSONArray();

        addTalkTags(hashtags);

        root.put("hashtags", hashtags);
    }

    private void addTalkTags(JSONArray tags) {
        StringBuilder combinedTitlesPhrase = new StringBuilder();

        for(ConferenceDay day : mConference.getDateList()) {
            for(TalkSlot slot : day.getTalkSlotList()) {
                if(slot.getEvent() != null) {
                    continue;
                }
                for(Talk talk : slot.getTalkList()) {
                    String name = talk.getName();
                    if(name == null || name.isEmpty()) {
                        continue;
                    }
                    combinedTitlesPhrase.append(name);
                    combinedTitlesPhrase.append(' ');
                }
            }
        }

        List<String> tagList = TagUtils.getInstance().extractTags(combinedTitlesPhrase.toString(), MAXIMUM_TAG_COUNT);
        Collections.sort(tagList);

        int orderCounter = 1;
        for(String tag : tagList) {
            JSONObject tagJSON = new JSONObject();
            tagJSON.put("color", COLOUR_PALETTE[orderCounter%COLOUR_PALETTE_LENGTH]);
            tagJSON.put("name", "#"+tag);
            tagJSON.put("description", "");
            tagJSON.put("order", orderCounter++);
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
