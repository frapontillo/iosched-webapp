package com.conferenceengineer.server.exporters.iosched13;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.ConferenceDay;
import com.conferenceengineer.server.datamodel.TalkSlot;

import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Exporter for speakers in the JSON format iosched wants
 */
public final class CommonSlotsJSON {

    private CommonSlotsJSON() {
        super();
    }

    /**
     * Create the export and place it in a string.
     */

    public static String export(final Conference conference) {
        JSONObject root = new JSONObject();
        JSONArray sessions = new JSONArray();

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

        for(ConferenceDay day : conference.getDateList()) {
            JSONArray slots = new JSONArray();
            for(TalkSlot slot : day.getTalkSlotList()) {
                if(slot.getEvent() == null) {
                    // Skip talk slots
                    continue;
                }

                JSONObject event = new JSONObject();
                event.put("start", timeFormatter.format(slot.getStart().getTime()));
                event.put("end", timeFormatter.format(slot.getEnd().getTime()));
                event.put("title", slot.getEvent());
                slots.put(event);
            }
            if(slots.length() == 0) {
                // Skip empty days
                continue;
            }

            JSONObject dayObject = new JSONObject();
            dayObject.put("date", dateFormatter.format(day.getDate()));
            dayObject.put("slot", slots);
            sessions.put(dayObject);
        }

        root.put("day", sessions);
        return root.toString();
    }
}
