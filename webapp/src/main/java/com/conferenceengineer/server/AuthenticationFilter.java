package com.conferenceengineer.server;

import com.conferenceengineer.server.datamodel.SystemUser;
import com.conferenceengineer.server.utils.EntityManagerWrapperBridge;
import com.conferenceengineer.server.utils.LoginUtils;

import javax.persistence.EntityManager;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter to verify the user is authenticated.
 */
public class AuthenticationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No init required
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        EntityManager em = EntityManagerWrapperBridge.getEntityManager(servletRequest);
        try {
            SystemUser user = LoginUtils.getInstance().getUserFromCookie((HttpServletRequest)servletRequest, em);
            if(user == null) {
                ((HttpServletResponse)servletResponse).sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            String username = user.getHumanName();
            if(username == null || username.isEmpty()) {
                username = user.getEmail();
            }
            servletRequest.setAttribute("username", username);
        } finally {
            em.close();
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // No destruction required
    }
}
