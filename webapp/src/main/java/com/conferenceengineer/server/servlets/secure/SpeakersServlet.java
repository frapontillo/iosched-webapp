package com.conferenceengineer.server.servlets.secure;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.LastModification;
import com.conferenceengineer.server.datamodel.LastModificationDAO;
import com.conferenceengineer.server.datamodel.Presenter;
import com.conferenceengineer.server.exporters.iosched13.SpeakersJSON;
import com.conferenceengineer.server.utils.ConferenceUtils;
import com.conferenceengineer.server.utils.EntityManagerWrapperBridge;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet to handle tracks
 */
public class SpeakersServlet extends IOSched13ExportServlet {

    private static final String EXPORT_FILENAME = "presenters.json";

    @Override
    protected String getExport(final Conference conference) {
        return SpeakersJSON.export(conference);
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

            String speakerId = request.getParameter("speakerId");
            Presenter presenter;
            if(speakerId == null) {
                presenter = new Presenter();
                presenter.setConference(conference);
            } else {
                presenter = em.find(Presenter.class, Integer.parseInt(speakerId));
            }


            presenter.setName(request.getParameter("name"));
            presenter.setImageURL(request.getParameter("imageURL"));
            presenter.setSocialLink(request.getParameter("socialURL"));
            presenter.setShortBiography(request.getParameter("shortBio"));
            presenter.setLongBiography(request.getParameter("longBio"));

            if(speakerId == null) {
                em.persist(presenter);
            }

            LastModificationDAO.getInstance().recordUpdate(em, conference, LastModification.NAME_SPEAKERS);

            em.getTransaction().commit();
        } finally {
            em.close();
        }

        response.sendRedirect("Speakers");
    }
}
