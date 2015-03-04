package com.conferenceengineer.server.servlets.secure;

/**
 * Display the conference secure to the user.
 */
public class Schedule extends DashboardBase {
    /**
     * Get the next page to send the user to.
     */

    @Override
    protected String getNextPage() {
        return "sessions.jsp";
    }
}
