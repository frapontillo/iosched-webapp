package com.conferenceengineer.server.servlets.secure;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.LastModification;
import com.conferenceengineer.server.datamodel.LastModificationDAO;
import com.conferenceengineer.server.datamodel.TalkLocation;
import com.conferenceengineer.server.exporters.iosched13.TalkLocationsJSON;
import com.conferenceengineer.server.utils.ConferenceUtils;
import com.conferenceengineer.server.utils.EntityManagerWrapperBridge;
import com.conferenceengineer.server.utils.ServletUtils;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet to handle tracks
 */
public class TalkLocationsServlet extends IOSched13ExportServlet {

    private static final String EXPORT_FILENAME = "rooms.json";

    @Override
    protected String getExport(final Conference conference) {
        return TalkLocationsJSON.export(conference);
    }

    @Override
    protected String getFilename() {
        return EXPORT_FILENAME;
    }

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws IOException, ServletException {
        EntityManager em = EntityManagerWrapperBridge.getEntityManager(request);
        try {
            em.getTransaction().begin();

            Conference conference = ConferenceUtils.getCurrentConference(request, em);

            TalkLocation location;
            String id = request.getParameter("id");
            boolean isNew = id == null || id.isEmpty();
            if(isNew) {
                location = new TalkLocation();
                location.setConference(conference);
            } else {
                location = em.find(TalkLocation.class, Integer.parseInt(id));
            }

            location.setName(request.getParameter("name"));
            location.setAddress(request.getParameter("address"));

            if(isNew) {
                em.persist(location);
            }

            LastModificationDAO.getInstance().recordUpdate(em, conference, LastModification.NAME_LOCATIONS);

            em.getTransaction().commit();
        } finally {
            em.close();
        }

        ServletUtils.redirectTo(request, response, "/secure/Rooms");
    }
}
