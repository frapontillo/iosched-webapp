package com.conferenceengineer.server.utils;

import com.conferenceengineer.server.datamodel.Conference;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Utils related to conference information
 */
public final class ConferenceUtils {

    /**
     * Private constructor to prevent instantiation.
     */

    private ConferenceUtils() {
        super();
    }

    /**
     * Get the Conference object representing the conference currently being
     * worked on by the authenticated user.
     *
     * @param request The request being serviced.
     * @param entityManager A currently active JPA EntityManager.
     */

    public static Conference getCurrentConference(final HttpServletRequest request,
                                                  final EntityManager entityManager) {
        HttpSession session = request.getSession(false);
        if(session == null) {
            return null;
        }

        Integer conferenceId = (Integer)session.getAttribute("conferenceId");
        if(conferenceId == null) {
            return null;
        }

        return entityManager.find(Conference.class, conferenceId);
    }
}
