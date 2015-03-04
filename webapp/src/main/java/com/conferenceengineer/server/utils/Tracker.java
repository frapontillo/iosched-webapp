package com.conferenceengineer.server.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Utilities to help track users
 */
public final class Tracker {

    private static final String TRACKER_COOKIE_NAME = "router";

    private Tracker() {
        super();
    }

    public static void setLocation(final HttpServletRequest request, final HttpServletResponse response,
                              final String location) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie: cookies) {
                if(TRACKER_COOKIE_NAME.equals(cookie.getName())) {
                    if(location.equals(cookie.getValue())) {
                        return;
                    }
                    cookie.setValue(location);
                    response.addCookie(cookie);
                    return;
                }
            }
        }

        Cookie newCookie = new Cookie(TRACKER_COOKIE_NAME, location);
        newCookie.setMaxAge(60*60*24*14);
        newCookie.setPath("/");
        response.addCookie(newCookie);
    }

    public static String getLocation(final HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie: request.getCookies()) {
                if(TRACKER_COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
