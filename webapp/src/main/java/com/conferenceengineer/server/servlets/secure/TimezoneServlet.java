package com.conferenceengineer.server.servlets.secure;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.utils.ConferenceUtils;
import com.conferenceengineer.server.utils.EntityManagerWrapperBridge;
import com.conferenceengineer.server.utils.ServletUtils;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for handling timezone changes
 */
public class TimezoneServlet extends HttpServlet{

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws IOException {
        EntityManager em = EntityManagerWrapperBridge.getEntityManager(request);
        try {
            Conference conference = ConferenceUtils.getCurrentConference(request, em);

            em.getTransaction().begin();
            conference.setTimezone(request.getParameter("timezone"));
            em.getTransaction().commit();

            request.getSession().setAttribute("message", "The time zone has been updated.");
        } finally {
            em.close();
        }

        ServletUtils.redirectTo(request, response, "/secure/Admin");
    }
}
