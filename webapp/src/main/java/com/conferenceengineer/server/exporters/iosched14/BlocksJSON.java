package com.conferenceengineer.server.exporters.iosched14;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.ConferenceDay;
import com.conferenceengineer.server.datamodel.LastModification;
import com.conferenceengineer.server.datamodel.Talk;
import com.conferenceengineer.server.datamodel.TalkSlot;
import com.conferenceengineer.server.exporters.AbstractIOSched14ManifestSubfile;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * Exporter for speakers in the JSON format iosched14 wants
 */
public final class BlocksJSON extends AbstractIOSched14ManifestSubfile {

    private static final String EXPORT_NAME = "blocks";

    private static final String[] DATA_DEPENDENCIES =
            {LastModification.NAME_TIME_SLOTS};

    private static final String TYPE_BREAK = "break",
                                TYPE_FREE = "free";

    private static final String EMPTY_TITLE = "";

    public BlocksJSON(final EntityManager em, final Conference conference) {
        super(em, conference);
    }

    public void addToJSONObject(final JSONObject root) {
        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        timeFormatter.setTimeZone(TimeZone.getTimeZone(mConference.getTimezone()));

        JSONArray slots = new JSONArray();
        for(ConferenceDay day : mConference.getDateList()) {
            for(TalkSlot slot : day.getTalkSlotList()) {
                if   (isKeynote(slot)) {
                    continue;
                }

                String title = slot.getEvent();
                String type = getType(slot);

                JSONObject event = new JSONObject();
                event.put("title", title == null ? EMPTY_TITLE : title);
                event.put("type", type);
                event.put("start", timeFormatter.format(slot.getStart().getTime()));
                event.put("end", timeFormatter.format(slot.getEnd().getTime()));
                slots.put(event);
            }
        }

        root.put("blocks", slots);
    }

    private String getType(final TalkSlot slot) {
        return slot.getEvent() == null ? TYPE_FREE : TYPE_BREAK;
    }

    private boolean isKeynote(TalkSlot slot) {
        List<Talk> talks = slot.getTalkList();
        if(talks == null || talks.isEmpty()) {
            return false;
        }

        for(Talk talk : talks) {
            Integer type = talk.getType();
            if(type != null && type == Talk.TYPE_KEYNOTE) {
                return true;
            }
        }

        return false;
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
