package com.conferenceengineer.server.barcamp;

import com.conferenceengineer.server.datamodel.Conference;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: alsutton
 * Date: 16/10/2013
 * Time: 11:33
 * To change this template use File | Settings | File Templates.
 */
public final class Utils {

    private Utils() {
        super();
    }

    /**
     * Gets the conference from the embedded ID in the URL
     */

    public static Conference getConferenceFromURL(final HttpServletRequest request, final EntityManager em) {
        String URI = request.getRequestURI();
        int lastSlash = URI.lastIndexOf('/');
        if(lastSlash == -1 || lastSlash == URI.length()-1) {
            return null;
        }

        String conferenceId = URI.substring(lastSlash+1);
        StringBuilder id = new StringBuilder();
        for(Character c : conferenceId.toCharArray()) {
            if(!Character.isDigit(c)) {
                break;
            }
            id.append(c);
        }

        if(id.length() == 0) {
            return null;
        }

        return em.find(Conference.class, Integer.parseInt(id.toString()));
    }
}
