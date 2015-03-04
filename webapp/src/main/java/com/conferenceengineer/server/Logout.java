package com.conferenceengineer.server;

import com.conferenceengineer.server.utils.LoginUtils;
import com.conferenceengineer.server.utils.ServletUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Logout extends HttpServlet {

    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws IOException {
        LoginUtils.getInstance().removeCookie(response);
        ServletUtils.redirectToIndex(request, response);
    }

}
