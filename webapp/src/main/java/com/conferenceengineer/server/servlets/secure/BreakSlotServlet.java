package com.conferenceengineer.server.servlets.secure;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.ConferenceDay;
import com.conferenceengineer.server.datamodel.LastModification;
import com.conferenceengineer.server.datamodel.LastModificationDAO;
import com.conferenceengineer.server.datamodel.TalkSlot;
import com.conferenceengineer.server.exporters.iosched13.CommonSlotsJSON;
import com.conferenceengineer.server.utils.EntityManagerWrapperBridge;
import com.conferenceengineer.server.utils.ServletUtils;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

/**
 * Servlet to handle tracks
 */
public class BreakSlotServlet extends IOSched13ExportServlet {

    private static final String EXPORT_FILENAME = "common_slots.json";

    @Override
    protected String getExport(final Conference conference) {
        return CommonSlotsJSON.export(conference);
    }

    @Override
    protected String getFilename() {
        return EXPORT_FILENAME;
    }

    /**
     * Post is add!!!
     */

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws IOException, ServletException {
        String action = request.getParameter("action");
        String append = "";
        if(action == null || action.isEmpty()) {
            String slotId = request.getParameter("slot");
            if(slotId == null) {
                append = add(request);
            } else {
                append = edit(request, slotId);
            }
        } else if("delete".equals(action)) {
            delete(request);
        }

        ServletUtils.redirectTo(request, response, "/secure/Schedule" + append);
    }

    private String add(final HttpServletRequest request) {
        EntityManager em = EntityManagerWrapperBridge.getEntityManager(request);
        try {
            em.getTransaction().begin();
            String conferenceDayIdString = request.getParameter("day");
            Integer conferenceDayId = Integer.parseInt(conferenceDayIdString);

            ConferenceDay conferenceDay = em.find(ConferenceDay.class, conferenceDayId);

            Calendar start = Calendar.getInstance();
            start.setTime(conferenceDay.getDate());
            setCalendarWithTime(start, request.getParameter("start"));

            Calendar end = Calendar.getInstance();
            end.setTime(conferenceDay.getDate());
            setCalendarWithTime(end, request.getParameter("end"));

            String event = request.getParameter("name");

            TalkSlot slot = new TalkSlot(conferenceDay, start, end, event);
            em.persist(slot);

            LastModificationDAO.getInstance().recordUpdate(em, conferenceDay.getConference(), LastModification.NAME_TIME_SLOTS);

            em.getTransaction().commit();

            return "#slot_"+slot.getId();
        } finally {
            em.close();
        }
    }

    private String edit(final HttpServletRequest request, final String slotId) {
        EntityManager em = EntityManagerWrapperBridge.getEntityManager(request);
        try {
            em.getTransaction().begin();
            TalkSlot slot = em.find(TalkSlot.class, Integer.parseInt(slotId));

            updateStartIfNeeded(request, slot);
            updateEndIfNeeded(request, slot);
            updateEventIfNeeded(request, slot);

            LastModificationDAO.getInstance().recordUpdate(em, slot.getConferenceDay().getConference(), LastModification.NAME_TIME_SLOTS);

            em.getTransaction().commit();

            return "#slot_"+slot.getId();
        } finally {
            em.close();
        }
    }

    private void updateEventIfNeeded(final HttpServletRequest request, final TalkSlot slot) {
        String event = request.getParameter("event");

        if(event != null) {
            slot.setEvent(event);
        }
    }

    private void updateStartIfNeeded(final HttpServletRequest request, final TalkSlot slot) {
        Calendar time = getCalendarForParameter(request, slot, "start");

        if(time != null) {
            slot.setStart(time);
        }
    }

    private void updateEndIfNeeded(final HttpServletRequest request, final TalkSlot slot) {
        Calendar time = getCalendarForParameter(request, slot, "end");
        if(time != null) {
            slot.setEnd(time);
        }
    }

    private Calendar getCalendarForParameter(final HttpServletRequest request, final TalkSlot slot, final String parameter) {
        String parameterValue = request.getParameter(parameter);
        if(parameterValue == null) {
            return null;
        }

        Calendar time = Calendar.getInstance();
        time.setTime(slot.getConferenceDay().getDate());
        setCalendarWithTime(time, request.getParameter(parameter));
        return time;
    }

    /**
     * Delete a slot
     */

    private void delete(final HttpServletRequest request)
        throws ServletException, IOException{
        String id = request.getParameter("id");
        if(id == null || id.isEmpty()) {
            request.getSession().setAttribute("error", "Session ID not specified");
            return;
        }

        EntityManager em = EntityManagerWrapperBridge.getEntityManager(request);
        try {
            em.getTransaction().begin();
            TalkSlot slot = em.find(TalkSlot.class, Integer.parseInt(id));

            LastModificationDAO.getInstance().recordUpdate(em, slot.getConferenceDay().getConference(), LastModification.NAME_TIME_SLOTS);

            em.remove(slot);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * Set a calendar with the time from a string.
     *
     * @param calendar The calendar to set
     * @param string The string holding the time in hh:mm format.
     */

    private void setCalendarWithTime(final Calendar calendar, final String string) {
        int idx = string.indexOf(':');
        if(idx == -1) {
            throw new RuntimeException("Invalid time format "+string);
        }

        int hours = Integer.parseInt(string.substring(0, idx));
        int mins = Integer.parseInt(string.substring(idx+1));
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, mins);
    }
}
