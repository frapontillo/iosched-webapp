package com.conferenceengineer.server.servlets.secure;

import com.conferenceengineer.server.utils.LoginUtils;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

public class Admin extends DashboardBase {

    private static final String DEFAULT_NEXT_PAGE = "admin.jsp";

    private static final String TIMEZONE_ATTRIBUTE = "timezones",
                                USER_ATTRIBUTE = "user";

    @Override
    protected void populateRequest(final HttpServletRequest request, final EntityManager em) {
        super.populateRequest(request, em);

        request.setAttribute(USER_ATTRIBUTE, LoginUtils.getInstance().getUserFromCookie(request, em));

        List<String> timezones = Arrays.asList(TimeZone.getAvailableIDs());
        Collections.sort(timezones);
        request.setAttribute(TIMEZONE_ATTRIBUTE, timezones);
    }

    @Override
    protected String getNextPage() {
        return DEFAULT_NEXT_PAGE;
    }
}
