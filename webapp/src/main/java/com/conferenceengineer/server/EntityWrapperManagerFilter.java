package com.conferenceengineer.server;

import com.conferenceengineer.server.datamodel.utils.EntityManagerFactoryWrapper;

import javax.servlet.*;
import java.io.IOException;

/**
 * Servlet filter which mnanages the EntityManagerWrapper lifecycle
 */
public class EntityWrapperManagerFilter implements Filter {
    /**
     * The attribute which holds the EntityManagerFactoryWrapper
     */

    public static final String EMFW_ATTRIBUTE = "fa_emfw";

    /**
     * The EntityManagerWrapper.
     */

    private EntityManagerFactoryWrapper mEntityManagerFactoryWrapper;

    /**
     * Initialise the EntityManagerWrapper.
     *
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        mEntityManagerFactoryWrapper = EntityManagerFactoryWrapper.getInstance();
    }

    /**
     * Ensure the factory wrapper is available to JSPs and servets
     *
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setAttribute(EMFW_ATTRIBUTE, mEntityManagerFactoryWrapper);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * Close the EntityManagerFactoryWrapper.
     *
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        mEntityManagerFactoryWrapper.close();
    }
}
