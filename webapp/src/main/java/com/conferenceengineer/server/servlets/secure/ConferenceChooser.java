package com.conferenceengineer.server.servlets.secure;

import com.conferenceengineer.server.datamodel.ConferencePermission;
import com.conferenceengineer.server.datamodel.SystemUser;
import com.conferenceengineer.server.utils.EntityManagerWrapperBridge;
import com.conferenceengineer.server.utils.LoginUtils;
import com.conferenceengineer.server.utils.ServletUtils;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Display the conference selection list to the user.
 */
public class ConferenceChooser extends HttpServlet {

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws IOException, ServletException {
        String id = request.getParameter("id");

        EntityManager em = EntityManagerWrapperBridge.getEntityManager(request);
        try {
            SystemUser currentUser = LoginUtils.getInstance().getUserFromCookie(request, em);

            boolean selectionHandled = false;
            if(id != null) {
                selectionHandled = handleSelection(request, currentUser, id);
            }

            if(selectionHandled) {
                ServletUtils.redirectTo(request, response, "/secure/Admin");
                return;
            }

            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("/secure/conference_chooser.jsp").forward(request, response);
        } finally {
            em.close();
        }
    }

    /**
     * Attempt to handle a users selection
     */

    private boolean handleSelection(final HttpServletRequest request, final SystemUser user, final String id) {
        int conferenceId = Integer.parseInt(id);
        for(ConferencePermission permission : user.getPermissions()) {
            if(permission.getConference().getId() == conferenceId) {
                request.getSession().setAttribute("conferenceId", conferenceId);
                return true;
            }
        }
        return false;
    }
}
