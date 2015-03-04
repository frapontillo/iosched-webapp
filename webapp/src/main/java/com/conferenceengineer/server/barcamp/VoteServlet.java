package com.conferenceengineer.server.barcamp;

import com.conferenceengineer.server.datamodel.*;
import com.conferenceengineer.server.utils.EntityManagerWrapperBridge;
import com.conferenceengineer.server.utils.LoginUtils;
import com.conferenceengineer.server.utils.ServletUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Servlet to handle a vote
 */
public class VoteServlet extends HttpServlet {

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws IOException {
        EntityManager em = EntityManagerWrapperBridge.getEntityManager(request);
        try {
            Conference conference = Utils.getConferenceFromURL(request, em);
            if(conference == null) {
                ServletUtils.redirectToIndex(request, response);
                return;
            }

            String vote = request.getParameter("vote");
            String talkId = request.getParameter("talk");
            if(vote == null || vote.isEmpty() || talkId == null || talkId.isEmpty()) {
                ServletUtils.redirectTo(request, response, "/barcamp/view/"+conference.getId());
                return;
            }


            SystemUser user = LoginUtils.getInstance().getUserFromCookie(request, em);
            Voter voter = VoterUtils.getVoter(request, em, user);
            if(voter == null) {
                HttpSession session = request.getSession(true);
                if(session.getAttribute("created_vid") != null) {
                    session.setAttribute("error", "Please enable cookies so your votes can be counted");
                    ServletUtils.redirectTo(request, response, "/barcamp/view/"+conference.getId());
                    return;
                }

                voter = VoterUtils.createVoter(response, em, user);
                session.setAttribute("created_vid", voter.getId());
                response.sendRedirect(request.getRequestURL()+"?"+request.getQueryString());
                return;
            }

            Talk talk = em.find(Talk.class, Integer.parseInt(talkId));

            if(talk.getConference().getId() != conference.getId()) {
                ServletUtils.redirectTo(request, response, "/barcamp/view/"+conference.getId());
                return;
            }

            em.getTransaction().begin();
            Query q = em.createQuery("SELECT x FROM TalkVote x WHERE x.talk = :talk AND x.voter = :voter");
            q.setParameter("talk", talk);
            q.setParameter("voter", voter);
            List<TalkVote> votes = q.getResultList();
            TalkVote talkVote;
            if(votes.isEmpty()) {
                talkVote = new TalkVote();
                talkVote.setTalk(talk);
                talkVote.setVoter(voter);
            } else {
                talkVote = votes.get(0);
            }
            talkVote.setVote(Integer.parseInt(vote));
            if(votes.isEmpty()) {
                em.persist(talkVote);
            }
            em.getTransaction().commit();

            ServletUtils.redirectTo(request, response, "/barcamp/view/"+conference.getId());
        } finally {
            em.close();
        }
    }
}
