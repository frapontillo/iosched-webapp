package com.conferenceengineer.server.barcamp;

import com.conferenceengineer.server.datamodel.*;
import com.conferenceengineer.server.utils.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

/**
 * The welcoming page for the barcamp system.
 */
public class Dashboard extends HttpServlet {
    /**
     * The Empty Map used to represent a non-existant voters votes
     */

    private static final Map<Integer, Object> EMPTY_VOTE_MAP = new HashMap<Integer, Object>();

    /**
     * The marker used to indicate votes
     */

    private static final Object VOTE_MARKER = new Object();

    /**
     * The counter used to ensure each voter gets a unique ID
     */

    private static int counter;

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws IOException, ServletException {

        EntityManager em = EntityManagerWrapperBridge.getEntityManager(request);
        try {
            Conference conference = Utils.getConferenceFromURL(request, em);
            if(conference == null) {
                ServletUtils.redirectToIndex(request, response);
                return;
            }

            Tracker.setLocation(request, response, "barcamp_"+conference.getId());
            Query q =
                    em.createQuery("SELECT x FROM Talk x WHERE x.conference = :conference AND x.slot IS NULL AND x.type = "+ Talk.TYPE_PROPOSED);
            q.setParameter("conference", conference);
            List<Talk> talks = (List<Talk>)q.getResultList();

            request.setAttribute("conference", conference);
            SystemUser user = LoginUtils.getInstance().getUserFromCookie(request, em);
            if(user != null) {
                request.setAttribute("user", user);
            }

            Voter voter = VoterUtils.getVoter(request, em, user);
            if(voter == null) {
                HttpSession session = request.getSession(true);
                String queryString = request.getQueryString();
                if(queryString == null || !queryString.contains("vid=true")) {
                    voter = VoterUtils.createVoter(response, em, user);
                    StringBuilder url = new StringBuilder();
                    url.append(request.getRequestURL());
                    url.append("?vid=true");
                    if(queryString != null) {
                        url.append('&');
                        url.append(queryString);
                    }
                    response.sendRedirect(url.toString());
                    return;
                } else {
                    session.setAttribute("error", "Please enable cookies so your votes can be counted");
                }
            }

            Map<Integer, Object> votes;
            if(voter != null) {
                votes = getUserVotes(em, voter, conference);
            } else {
                votes = EMPTY_VOTE_MAP;
            }

/*            List<Talk> randomSource = new ArrayList<Talk>(talks);
            List<TalkHolder> randomisedList = new ArrayList<TalkHolder>(talks.size());
            Random random = new Random();
            while(!randomSource.isEmpty()) {
                Talk thisTalk = randomSource.remove(random.nextInt(randomSource.size()));
                boolean votedFor = votes.get(thisTalk.getId()) != null;
                TalkHolder holder = new TalkHolder(thisTalk, votedFor);
                randomisedList.add(holder);
            }
*/
            List<TalkHolder> randomisedList = new ArrayList<TalkHolder>(talks.size());
            for(Talk thisTalk : talks) {
                boolean votedFor = votes.get(thisTalk.getId()) != null;
                TalkHolder holder = new TalkHolder(thisTalk, votedFor);
                randomisedList.add(holder);
            }
            request.setAttribute("talks", randomisedList);

            request.getRequestDispatcher("/barcamp/admin.jsp").forward(request, response);
        } finally {
            em.close();
        }
    }

    /**
     * Get the list of votes the voter has cast
     */

    private Map<Integer,Object> getUserVotes(final EntityManager em, final Voter voter, final Conference conference) {
        Query q =
                em.createQuery("SELECT x FROM TalkVote x WHERE x.talk.conference = :conference AND x.voter = :voter");
        q.setParameter("voter", voter);
        q.setParameter("conference", conference);

        Map<Integer, Object> votes = new HashMap<Integer,Object>();
        for(TalkVote vote : (List<TalkVote>)q.getResultList()) {
            if(vote.getVote() != 0) {
                votes.put(vote.getTalk().getId(), VOTE_MARKER);
            }
        }

        return votes;
    }


    public class TalkHolder {
        public Talk talk;
        public boolean votedFor;

        TalkHolder(final Talk talk, final boolean votedFor) {
            this.talk = talk;
            this.votedFor = votedFor;
        }

        public Talk getTalk() {
            return talk;
        }

        public boolean isVotedFor() {
            return votedFor;
        }
    }

}
