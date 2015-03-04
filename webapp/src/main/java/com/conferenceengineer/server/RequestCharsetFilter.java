package com.conferenceengineer.server;

import javax.servlet.*;
import java.io.IOException;

/**
 * By default parameter requests come in with ISO-8859-1, we want to use UTF-8 so
 * we need to set the encoding in the request.
 */
public class RequestCharsetFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Do nothing
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // Do nothing
    }
}
