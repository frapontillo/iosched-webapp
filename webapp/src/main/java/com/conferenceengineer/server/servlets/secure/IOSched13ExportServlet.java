package com.conferenceengineer.server.servlets.secure;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.utils.ConferenceUtils;
import com.conferenceengineer.server.utils.EntityManagerWrapperBridge;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet which creates an export for the IOSched13 app.
 */
public abstract class IOSched13ExportServlet extends HttpServlet {

    /**
     * Get the JSON in a format suitable for IOSched
     */

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        String json;

        EntityManager em = EntityManagerWrapperBridge.getEntityManager(request);
        try {
            Conference conference = ConferenceUtils.getCurrentConference(request, em);
            if(conference == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            json = getExport(conference);
        } finally {
            em.close();
        }

        response.setHeader("Content-Disposition", "attachment; filename=\""+getFilename()+"\"");
        response.getOutputStream().write(json.getBytes("UTF-8"));
    }

    abstract protected String getExport(Conference conference);
    abstract protected String getFilename();
}
