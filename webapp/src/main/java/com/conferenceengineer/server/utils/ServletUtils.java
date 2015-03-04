package com.conferenceengineer.server.utils;

import com.conferenceengineer.server.datamodel.ConferencePermission;
import com.conferenceengineer.server.datamodel.SystemUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ServletUtils {

    private ServletUtils() {
        super();
    }

    public static void redirectToIndex(final HttpServletRequest request, final HttpServletResponse response)
        throws IOException {
        response.sendRedirect("/"+request.getContextPath());
    }

    public static void redirectTo(final HttpServletRequest request, final HttpServletResponse response,
                                  final String path)
        throws IOException {
        String contextPath = request.getContextPath();
        if(contextPath.endsWith("/")) {
            contextPath = contextPath.substring(contextPath.length()-1);
        }
        String redirectPath = contextPath+path;
        if(redirectPath.startsWith("//")) {
            redirectPath = redirectPath.substring(1);
        }
        response.sendRedirect(redirectPath);
    }

    public static void sendUserToPostLoginPage(final HttpServletRequest request, final HttpServletResponse response,
                                               final SystemUser user)
            throws IOException {
        LoginUtils.getInstance().addCookie(response, user);

        String area = Tracker.getLocation(request);
        if (area != null && area.startsWith("barcamp_")) {
            ServletUtils.redirectTo(request, response, "/barcamp/view/" + area.substring(8));
            return;
        }

        List<ConferencePermission> permissions = user.getPermissions();
        if (permissions == null || permissions.size() != 1) {
            ServletUtils.redirectTo(request, response, "/secure/conference");
        } else {
            request.getSession().setAttribute("conferenceId", permissions.get(0).getConference().getId());
            ServletUtils.redirectTo(request, response, "/secure/Admin");
        }
    }

    public static void storeErrorInSession(final HttpServletRequest request, final String message) {
        request.getSession().setAttribute("error", message);
    }
}
