package com.conferenceengineer.server.exporters.iosched14;

import com.conferenceengineer.server.datamodel.*;
import com.conferenceengineer.server.exporters.AbstractIOSched14ManifestSubfile;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SessionsJSON extends AbstractIOSched14ManifestSubfile {

    private static final String EXPORT_NAME = "session_data";
    private static final String[] DATA_DEPENDENCIES = {LastModification.NAME_TALKS, LastModification.NAME_SPEAKERS};

    private static final String[] SINGLE_TRACK_CONFERENCE_COLOURS = { "#ff9800", "#009688" };

    private static final String TAG_FOR_SESSION = "TYPE_SESSION";

    private final SimpleDateFormat mTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    public SessionsJSON(final EntityManager em, Conference conference) {
        super(em, conference);
        mTimeFormatter.setTimeZone(TimeZone.getTimeZone(mConference.getTimezone()));
    }

    public void addToJSONObject(final JSONObject root) {
        JSONArray sessions = new JSONArray();

        boolean isSingleTracked = isConferenceSingleTracked();
        int singleTrackColourIdx = 0;

        for(ConferenceDay day : mConference.getDateList()) {
            for(TalkSlot slot : day.getTalkSlotList()) {
                if(slot.getEvent() != null) {
                    // Skip event slots
                    continue;
                }

                String slotColour = SINGLE_TRACK_CONFERENCE_COLOURS[singleTrackColourIdx%2];
                singleTrackColourIdx++;

                for(Talk talk : slot.getTalkList()) {
                    JSONObject json = new JSONObject();
                    json.put("id", Integer.toString(talk.getId()));

                    String link = talk.getInformationURL();
                    if(link != null) {
                        json.put("url", link);
                    }

                    json.put("title", talk.getName());
                    json.put("description", talk.getShortDescription());


                    Integer type = talk.getType();
                    boolean isKeynote = (type != null && type == Talk.TYPE_KEYNOTE);

                    JSONArray tags = new JSONArray();

                    Track track = talk.getTrack();
                    if(track != null) {
                        tags.put(TAG_FOR_SESSION);
                        tags.put(TagsJSON.CATEGORY_THEME + "_" + talk.getTrack().getId());
                    } else {
                        if(isKeynote) {
                            tags.put("FLAG_KEYNOTE");
                        }
                    }
                    json.put("tags", tags);
                    // TODO: tags

                    if(isKeynote) {
                        json.put("mainTag", "FLAG_KEYNOTE");
                    }
                    // TODO: mainTag

                    addStartAndEndTimeSlots(json, slot);

                    // TODO: Offer a photoUrl
                    if(!talk.getPresenters().isEmpty()) {
                        Iterator<Presenter> presenters = talk.getPresenters().iterator();
                        if(presenters.hasNext()) {
                            json.put("photoUrl", presenters.next().getImageURL());
                        }
                    }


                    // TODO: youTubeUrl

                    JSONArray presenterIds = new JSONArray();
                    for(Presenter presenter : talk.getPresenters()) {
                        presenterIds.put(Integer.toString(presenter.getId()));
                    }
                    json.put("speakers", presenterIds);

                    if(talk.getLocation() == null) {
                        Logger.getAnonymousLogger().log(Level.WARNING, "No location for " + talk.getName());
                    } else {
                        json.put("room", "ROOM"+talk.getLocation().getId());
                    }

                    json.put("isLivestream", Boolean.FALSE);

                    // TODO: captionsUrl

                    if(track != null && track.getColour() != null) {
                        String colour;
                        if(isSingleTracked) {
                            colour = slotColour;
                        } else {
                            colour = track.getColour();
                        }
                        json.put("color", colour);
                    }

                    // TODO: hashtag

                    sessions.put(json);
                }
            }
        }

        root.put("sessions", sessions);
    }

    @Override
    protected String getExportName() {
        return EXPORT_NAME;
    }

    @Override
    protected String[] getDependantDataNames() {
        return DATA_DEPENDENCIES;
    }

    private boolean isConferenceSingleTracked() {
        List<Track> tracks = mConference.getTrackList();
        if(tracks == null || tracks.isEmpty() || tracks.size() == 1) {
            return true;
        }

        boolean encounteredPreviousTracksWithTalks = false;
        for(Track track : tracks) {
            if(!track.getTalkList().isEmpty()) {
                if(encounteredPreviousTracksWithTalks) {
                    return false;
                } else {
                    encounteredPreviousTracksWithTalks = true;
                }
            }
        }

        return true;
    }

    private void addStartAndEndTimeSlots(final JSONObject object, final TalkSlot talkSlot) {
        addTimeslot(object, "startTimestamp", talkSlot.getStart());
        addTimeslot(object, "endTimestamp", talkSlot.getEnd());
    }

    private void addTimeslot(final JSONObject object, final String property, final Calendar calendar) {
        object.put(property, createTimestamp(calendar));
    }

    private String createTimestamp(final Calendar calendar) {
        synchronized (mTimeFormatter) {
            return mTimeFormatter.format(calendar.getTime());
        }
    }
}
