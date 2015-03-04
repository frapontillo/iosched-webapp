package com.conferenceengineer.server.servlets.webschedule;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.exporters.html.ScheduleExporter;
import com.conferenceengineer.server.utils.EntityManagerWrapperBridge;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Schedule extends HttpServlet {

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ServletException {
        String conferenceId = request.getParameter("conference");
        if(conferenceId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        response.setCharacterEncoding("UTF-8");

        EntityManager em = EntityManagerWrapperBridge.getEntityManager(request);
        try {
            Conference conference = getConference(em, conferenceId);
            if(conference == null) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Unable to get conference for " + conferenceId);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            String export = new ScheduleExporter(conference).createExport();

            response.setHeader("Content-Disposition", "attachment; filename=\"schedule.html\"");
            response.getOutputStream().write(export.getBytes("UTF-8"));
        } finally {
            em.close();
        }
    }

    private Conference getConference(final EntityManager entityManager, final String conferenceIdStrung) {
        Integer conferenceId = Integer.parseInt(conferenceIdStrung);
        return entityManager.find(Conference.class, conferenceId);
    }

}
