package com.conferenceengineer.server.servlets.secure;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.LastModification;
import com.conferenceengineer.server.datamodel.LastModificationDAO;
import com.conferenceengineer.server.datamodel.Track;
import com.conferenceengineer.server.exporters.iosched13.TracksJSON;
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
public class TracksServlet extends IOSched13ExportServlet {

    private static final String EXPORT_FILENAME = "tracks.json";

    @Override
    protected String getExport(final Conference conference) {
        return TracksJSON.export(conference);
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

            Track track;
            String trackId = request.getParameter("id");
            boolean isNew = (trackId == null || trackId.isEmpty());
            if(isNew) {
                track = new Track();
                track.setConference(conference);
            } else {
                track = em.find(Track.class, Integer.parseInt(trackId));
            }

            track.setName(request.getParameter("name"));
            String colour = request.getParameter("colour");
            if(colour != null) {
                colour = colour.trim();
            }
            track.setColour(colour);
            track.setDescription(request.getParameter("description"));

            if(isNew) {
                em.persist(track);
            }

            LastModificationDAO.getInstance().recordUpdate(em, conference, LastModification.NAME_TRACKS);

            em.getTransaction().commit();
        } finally {
            em.close();
        }

        ServletUtils.redirectTo(request, response, "/secure/Tracks");
    }
}
