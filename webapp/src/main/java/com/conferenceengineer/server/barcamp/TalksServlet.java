package com.conferenceengineer.server.barcamp;

import com.conferenceengineer.server.datamodel.*;
import com.conferenceengineer.server.utils.EntityManagerWrapperBridge;
import com.conferenceengineer.server.utils.LoginUtils;
import com.conferenceengineer.server.utils.ServletUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Servlet to handle barcamp talks
 */
public class TalksServlet extends HttpServlet {

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws IOException, ServletException {
        EntityManager em = EntityManagerWrapperBridge.getEntityManager(request);
        try {
            Conference conference = Utils.getConferenceFromURL(request, em);
            if(conference == null) {
                ServletUtils.redirectToIndex(request, response);
                return;
            }

            SystemUser user = LoginUtils.getInstance().getUserFromCookie(request, em);
            if(user != null) {
                String talkId = request.getParameter("talkId");
                em.getTransaction().begin();
                try {
                    if(talkId == null) {
                        add(em, conference, request);
                    } else {
                        edit(em, request, talkId);
                    }
                    em.getTransaction().commit();
                } finally {
                    EntityTransaction transaction = em.getTransaction();
                    if(transaction.isActive()) {
                        transaction.rollback();
                    }
                }

                request.getSession().setAttribute("message", "Your submission has added to the list of proposals.");
            }

            ServletUtils.redirectTo(request, response, "/barcamp/view/" + conference.getId());
        } finally {
            em.close();
        }

    }

    /**
     * Handle an add
     */

    private void add(final EntityManager em, final Conference conference, final HttpServletRequest request) {
        Talk talk = new Talk();
        populateObject(talk, request);
        talk.setConference(conference);
        em.persist(talk);
        addPresenter(em, conference, talk, request);
    }

    /**
     * Handle an edit
     */

    private void edit(final EntityManager em, final HttpServletRequest request, final String slotId) {
        Talk talk = em.find(Talk.class, Integer.parseInt(slotId));
        String presenterId = request.getParameter("presenter");
        if(presenterId != null) {
            // We're just altering a presenter
            editPresenter(em, request, talk, presenterId);
            return;
        }

        // We're updating the object.
        populateObject(talk, request);
    }


    private void editPresenter(final EntityManager em, final HttpServletRequest request, final Talk talk,
                               final String presenterId) {
        if(request.getParameter("delete") != null) {
            deletePresenter(em, talk, presenterId);
        }
    }

    /**
     * Populate the object with the information from the request.
     */

    private void populateObject(final Talk talk, final HttpServletRequest request) {
        talk.setName(request.getParameter("title"));
        talk.setShortDescription(request.getParameter("description"));
        talk.setType(Talk.TYPE_PROPOSED);
    }

    /**
     * Add a presenter to a talk
     */

    private void addPresenter(final EntityManager em, final Conference conference,
                              final Talk talk, final HttpServletRequest request) {
        SystemUser user = LoginUtils.getInstance().getUserFromCookie(request, em);

        Query q = em.createQuery("SELECT x FROM Presenter x WHERE x.conference = :conference AND x.user = :user");
        q.setParameter("conference", conference);
        q.setParameter("user", user);
        q.setMaxResults(1);
        List<Presenter> presenterList = (List<Presenter>)q.getResultList();
        Presenter presenter;
        if(presenterList.isEmpty()) {
            presenter = new Presenter();
            presenter.setConference(conference);
            presenter.setName(user.toString());
            Set<Talk> talks = new HashSet<Talk>();
            talks.add(talk);
            presenter.setTalks(talks);
            em.persist(presenter);
        } else {
            presenter = presenterList.get(0);
            talk.getPresenters().add(presenter);
        }

    }

    private void deletePresenter(final EntityManager em, final Talk talk, final String presenterId) {
        Presenter presenter = em.find( Presenter.class, Integer.parseInt(presenterId));
        talk.getPresenters().remove(presenter);
    }
}
