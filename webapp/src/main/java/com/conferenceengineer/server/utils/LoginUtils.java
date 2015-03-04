package com.conferenceengineer.server.utils;

import com.conferenceengineer.server.datamodel.SystemUser;
import com.conferenceengineer.server.datamodel.SystemUserDAO;
import com.conferenceengineer.server.datamodel.UserAuthenticationInformation;
import com.conferenceengineer.server.datamodel.UserAuthenticationInformationDAO;
import com.conferenceengineer.server.utils.authentication.InternalUserAuthenticationVerifier;
import com.conferenceengineer.server.utils.authentication.UserAuthenticationVerifier;

import javax.persistence.EntityManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilities for handling the users login information
 */
public final class LoginUtils {

    /**
     * The name of the cookie to use.
     */

    private static final String COOKIE_NAME = "ce_current";

    /**
     * The authentication verifier classes
     */

    private static final Map<Integer,UserAuthenticationVerifier> authenticationVerifiers =
             new HashMap<Integer, UserAuthenticationVerifier>();
    static {
        authenticationVerifiers.put(
                UserAuthenticationInformationDAO.AUTHENTICATOR_INTERNAL,
                new InternalUserAuthenticationVerifier()
        );
    }

    /**
     * Private constructor to enforce singleton pattern.
     */

    private LoginUtils() {
        super();
    }

    /**
     * Add the cookie to a response
     *
     * @param response The response to add the cookie to.
     * @param user The user to create the cookie for.
     */

    public void addCookie(final HttpServletResponse response, final SystemUser user) {
        response.addCookie(createCookie(user));
    }

    /**
     * Expire the cookie.
     *
     * @param response The response to use to expire the cookie.
     */

    public void removeCookie(final HttpServletResponse response) {
        response.addCookie(createCookie(null));
    }

    /**
     * Get the user cookie value
     *
     * @param request The request holding the cookie
     * @return The user details
     */

    public SystemUser getUserFromCookie(final HttpServletRequest request, final EntityManager em) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            return null;
        }


        for(Cookie cookie : request.getCookies()) {
            if(COOKIE_NAME.equals(cookie.getName())) {
                int userId = Integer.parseInt(cookie.getValue());
                return SystemUserDAO.getInstance().get(em, userId);
            }
        }

        return null;
    }


    /**
     * Construct the login details cookie.
     *
     * @param user The user to create the Cookie for.
     * @return The Cookie to use.
     */

    public Cookie createCookie(final SystemUser user) {
        Cookie cookie;
        if( user != null ) {
            cookie = new Cookie(COOKIE_NAME, Integer.toString(user.getId()));
            cookie.setMaxAge(60*60*24*14);
            cookie.setPath("/");
        } else {
            cookie = new Cookie(COOKIE_NAME, "");
            cookie.setMaxAge(0);
        }
        cookie.setPath("/");
        return cookie;
    }

    /**
     * Verify a user given an authenticator
     *
     * @param username The username to test.
     * @param password The password to test.
     * @param authenticatorInformation The information from the database.
     */

    public boolean isUserValid(final String username, final String password,
                               final UserAuthenticationInformation authenticatorInformation) {
        UserAuthenticationVerifier verifier
                = authenticationVerifiers.get(authenticatorInformation.getAuthenticatorType());
        return verifier != null && verifier.isUserAuthenticated(username, password, authenticatorInformation);
    }

    //--------- Singleton

    private static final class InstanceHolder {
        private static final LoginUtils INSTANCE = new LoginUtils();
    }

    public static LoginUtils getInstance() {
        return InstanceHolder.INSTANCE;
    }
}
