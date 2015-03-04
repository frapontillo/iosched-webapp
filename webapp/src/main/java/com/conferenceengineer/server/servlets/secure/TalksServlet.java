package com.conferenceengineer.server.servlets.secure;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.LastModification;
import com.conferenceengineer.server.datamodel.LastModificationDAO;
import com.conferenceengineer.server.datamodel.Presenter;
import com.conferenceengineer.server.datamodel.Talk;
import com.conferenceengineer.server.datamodel.TalkLocation;
import com.conferenceengineer.server.datamodel.TalkSlot;
import com.conferenceengineer.server.datamodel.TalkVote;
import com.conferenceengineer.server.datamodel.Track;
import com.conferenceengineer.server.exporters.iosched13.SessionsJSON;
import com.conferenceengineer.server.utils.ConferenceUtils;
import com.conferenceengineer.server.utils.EntityManagerWrapperBridge;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

/**
 * Servlet to handle tracks
 */
public class TalksServlet extends IOSched13ExportServlet {

    private static final String EXPORT_FILENAME = "sessions.json";

    @Override
    protected String getExport(final Conference conference) {
        return SessionsJSON.export(conference);
    }

    @Override
    protected String getFilename() {
        return EXPORT_FILENAME;
    }

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws IOException, ServletException {
        String append = "";

        EntityManager em = EntityManagerWrapperBridge.getEntityManager(request);
        try {
            em.getTransaction().begin();
            String action = request.getParameter("action");
            String talkId = request.getParameter("talkId");

            LastModificationDAO
                    .getInstance()
                        .recordUpdate(
                                em,
                                ConferenceUtils.getCurrentConference(request, em),
                                LastModification.NAME_TALKS
                        );

            if(action != null && "delete".equals(action)) {
                Talk talk = em.find(Talk.class, Integer.parseInt(talkId));
                if(talk != null) {
                    delete(em, talk);
                }
            } else {
                if(talkId == null) {
                    append=add(em, request);
                } else {
                    append=edit(em, request, talkId);
                }
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }

        String next = request.getParameter("next");
        if(next == null) {
            next = "Schedule"+append;
        }
        response.sendRedirect(next);
    }

    /**
     * Handle a delete
     */

    private void delete(final EntityManager em, final Talk talk) {
        Query query = em.createQuery("SELECT x FROM TalkVote x WHERE x.talk = :talk");
        query.setParameter("talk", talk);
        for(TalkVote vote : (List<TalkVote>)query.getResultList()) {
            em.remove(vote);
        }
        talk.getPresenters().clear();
        em.remove(talk);
    }

    /**
     * Handle an add
     */

    private String add(final EntityManager em, final HttpServletRequest request) {
        Talk talk = new Talk();
        talk.setPresenters(new HashSet<Presenter>());
        populateObject(em, talk, request);
        addPresenter(em, talk, request);
        em.persist(talk);
        return "#slot_"+talk.getSlot().getId();
    }

    /**
     * Handle an edit
     */

    private String edit(final EntityManager em, final HttpServletRequest request, final String slotId) {
        Talk talk = em.find(Talk.class, Integer.parseInt(slotId));
        String presenterId = request.getParameter("presenter");
        if(presenterId != null) {
            // We're just altering a presenter
            editPresenter(em, request, talk, presenterId);
        } else {
            // We're updating the object.
            populateObject(em, talk, request);
        }
        return "#slot_"+talk.getSlot().getId();
    }


    private void editPresenter(final EntityManager em, final HttpServletRequest request, final Talk talk,
                               final String presenterId) {
        if(request.getParameter("delete") != null) {
            deletePresenter(em, talk, presenterId);
        } else {
            addPresenter(em, talk, presenterId);
        }
    }

    /**
     * Populate the object with the information from the request.
     */

    private void populateObject(final EntityManager em, final Talk talk, final HttpServletRequest request) {
        String locationString = request.getParameter("location");
        if(locationString != null) {
            try {
                TalkLocation location = em.find(TalkLocation.class, Integer.parseInt(locationString));
                talk.setLocation(location);
            } catch(NumberFormatException e) {
                log("Problem parsing location ID", e);
            }
        }

        String slotId = request.getParameter("slot");
        if(slotId != null) {
            TalkSlot slot = em.find(TalkSlot.class, Integer.parseInt(slotId));
            talk.setSlot(slot);
        }

        int track = Integer.parseInt(request.getParameter("track"));
        if(track == -1) {
            talk.setType(Talk.TYPE_KEYNOTE);
            talk.setTrack(null);
        } else {
            talk.setType(Talk.TYPE_ACCEPTED);
            talk.setTrack(em.find(Track.class, track));
        }

        talk.setName(request.getParameter("title"));
        talk.setInformationURL(request.getParameter("infoURL"));
        talk.setShortDescription(request.getParameter("description"));
    }

    /**
     * Add a presenter to a talk
     */

    private void addPresenter(final EntityManager em, final Talk talk, final HttpServletRequest request) {
        addPresenter(em,talk,request.getParameter("presenter"));
    }

    private void addPresenter(final EntityManager em, final Talk talk, final String presenterId) {
        Presenter presenter = em.find( Presenter.class, Integer.parseInt(presenterId));
        talk.getPresenters().add(presenter);
    }

    private void deletePresenter(final EntityManager em, final Talk talk, final String presenterId) {
        Presenter presenter = em.find( Presenter.class, Integer.parseInt(presenterId));
        talk.getPresenters().remove(presenter);
    }
}
