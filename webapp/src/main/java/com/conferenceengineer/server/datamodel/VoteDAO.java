package com.conferenceengineer.server.datamodel;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Data Accessor Object for Vote objects.
 */
public class VoteDAO {

    /**
     * Store the information about a vote.
     *
     * @param entityManager The currently active EntityManager
     * @param voteSeries The series the vote applies to.
     * @param systemUser The user this vote is for.
     */
    public void store(final EntityManager entityManager, final VoteSeries voteSeries,
                      final SystemUser systemUser) {
        entityManager.persist(new Vote(voteSeries, systemUser));
    }

    /**
     * Get the votes associated with a specific object.
     *
     */

    @SuppressWarnings("unchecked")
    public Vote getVote(final EntityManager entityManager, final VoteSeries voteSeries,
                                 final SystemUser systemUser) {
        Query q = entityManager.createQuery("SELECT x FROM Vote x WHERE x.voteSeries = :voteSeries AND x.systemUser = :systemUser");
        q.setParameter("voteSeries", voteSeries);
        q.setParameter("systemUser", systemUser);
        List<Vote> votes = (List<Vote>)q.getResultList();
        if(votes.size() > 1) {
            throw new RuntimeException("Multiple users found for "+systemUser.getId()+" in "+voteSeries.getId());
        }
        if(votes.isEmpty()) {
            return null;
        }
        return votes.get(0);
    }
}
