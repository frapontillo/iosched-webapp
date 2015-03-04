package com.conferenceengineer.server.servlets.secure;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.LastModification;
import com.conferenceengineer.server.datamodel.LastModificationDAO;
import com.conferenceengineer.server.utils.ConferenceUtils;
import com.conferenceengineer.server.utils.EntityManagerWrapperBridge;
import com.conferenceengineer.server.utils.ServletUtils;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SetConferenceHashtagServlet extends HttpServlet {

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        EntityManager em = EntityManagerWrapperBridge.getEntityManager(request);
        try {
            Conference conference = ConferenceUtils.getCurrentConference(request, em);

            em.getTransaction().begin();
            conference.setHashtag(request.getParameter("hashtag"));
            LastModificationDAO.getInstance().recordUpdate(em, conference, LastModification.NAME_HASHTAGS);
            em.getTransaction().commit();

            request.getSession().setAttribute("message", "The hashtag has been updated.");
        } finally {
            em.close();
        }

        ServletUtils.redirectTo(request, response, "/secure/Admin");
    }
}
