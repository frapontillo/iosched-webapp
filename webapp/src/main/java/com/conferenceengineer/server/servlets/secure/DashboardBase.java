package com.conferenceengineer.server.servlets.secure;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.utils.ConferenceUtils;
import com.conferenceengineer.server.utils.EntityManagerWrapperBridge;
import com.conferenceengineer.server.utils.ServletUtils;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class DashboardBase extends HttpServlet {

    private static final String DEFAULT_PAGE_FOR_EXPIRED_SESSIONS = "/login.jsp";

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");

        EntityManager em = EntityManagerWrapperBridge.getEntityManager(request);
        try {
            populateRequest(request, em);

            request.getRequestDispatcher("/secure/" + getNextPage()).forward(request, response);
        } catch(SessionExpiredException e) {
            request.getSession(true).setAttribute("error", "Your session timed out. Please log in again");
            ServletUtils.redirectTo(request, response, DEFAULT_PAGE_FOR_EXPIRED_SESSIONS);
        } finally {
            em.close();
        }
    }

    /**
     * Method to add attributes to the request object to provide commonly used information.
     */

    protected void populateRequest(final HttpServletRequest request, final EntityManager em) {
        Conference conference = ConferenceUtils.getCurrentConference(request, em);
        if(conference == null) {
            throw new SessionExpiredException();
        }
        request.setAttribute( "conference", conference );
    }

    protected abstract String getNextPage();

    private static class SessionExpiredException extends RuntimeException {}
}
