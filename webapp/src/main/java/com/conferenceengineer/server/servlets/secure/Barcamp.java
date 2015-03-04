package com.conferenceengineer.server.servlets.secure;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.ConferenceDay;
import com.conferenceengineer.server.datamodel.Talk;
import com.conferenceengineer.server.datamodel.TalkSlot;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

public class Barcamp extends DashboardBase {
    /**
     * Comparator to sort the talks by the number of votes with the highest voted coming first.
     */

    private static Comparator<TalkHolder> VOTE_SORTER = new Comparator<TalkHolder>() {
        @Override
        public int compare(TalkHolder talk1, TalkHolder talk2) {
            return talk2.getVotes() - talk1.getVotes();
        }
    };

    @Override
    protected void populateRequest(final HttpServletRequest request, final EntityManager em) {
        super.populateRequest(request, em);

        Conference conference = (Conference) request.getAttribute("conference");
        Query q =
                em.createQuery("SELECT x FROM Talk x WHERE x.conference = :conference AND x.slot IS NULL AND x.type = "+ Talk.TYPE_PROPOSED);
        q.setParameter("conference", conference);
        List<Talk> talkJPAResults = (List<Talk>)q.getResultList();
        List<TalkHolder> talks = new ArrayList<TalkHolder>(talkJPAResults.size());
        if(talkJPAResults != null) {
            Query countQuery = em.createQuery("SELECT COUNT(x.id) FROM TalkVote x WHERE x.talk = :talk");
            for(Talk talk : talkJPAResults) {
                countQuery.setParameter("talk", talk);
                talks.add(new TalkHolder(talk, ((Number)countQuery.getSingleResult()).intValue()));
            }
        }
        Collections.sort(talks, VOTE_SORTER);
        request.setAttribute("sortedTalks", talks);

        List<ScheduleSlotHolder> slots = new ArrayList<ScheduleSlotHolder>();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        for(ConferenceDay day:conference.getDateList()) {
            for(TalkSlot slot : day.getTalkSlotList()) {
                StringBuilder builder = new StringBuilder();
                builder.append(dayFormat.format(day.getDate()));
                builder.append(", ");
                builder.append(timeFormat.format(slot.getStart().getTime()));
                builder.append('-');
                builder.append(timeFormat.format(slot.getEnd().getTime()));

                slots.add(new ScheduleSlotHolder(slot.getId(), builder.toString()));
            }
        }
        request.setAttribute("slots", slots);

        request.setAttribute("serverStatus", "All servers are operational");
        request.setAttribute("serverStatusType", "Good");
    }

    /**
     * Get the next page to send the user to.
     */

    @Override
    protected String getNextPage() {
        return "barcamp.jsp";
    }

    /**
     * Wrapper for the talk information
     */

    public class TalkHolder {
        private int votes;
        private Talk talk;

        TalkHolder(final Talk talk, int votes) {
            this.talk = talk;
            this.votes = votes;
        }

        public int getVotes() {
            return votes;
        }

        public Talk getTalk() {
            return talk;
        }
    }

    /**
     * Wrapper for a schedule slot
     */

    public class ScheduleSlotHolder {
        private int id;
        private String name;

        ScheduleSlotHolder(final int id, final String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
