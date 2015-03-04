package com.conferenceengineer.server.datamodel;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Data Accessor Object for VoteSeries
 */
public class VoteSeriesDAO {

    /**
     * The relationship indicators.
     */

    public static final int RELATION_TO_NOTHING = 0,
                            RELATION_TO_CONFERENCE = 1;

    /**
     * Store the information about a vote series.
     *
     * @param entityManager The currently active EntityManager
     * @param title The title of the voting series.
     * @param description The description of the voting series
     * @param relationTo The type of object this vote series relates to.
     * @param relationId The ID of the object this series is related to.
     */
    public void store(final EntityManager entityManager, final String title, final String description,
                      final int relationTo, final int relationId) {
        entityManager.persist(new VoteSeries(title, description, relationTo, relationId));
    }

    /**
     * Get the votes associated with a specific object.
     *
     */

    @SuppressWarnings("unchecked")
    public List<VoteSeries> getByEmail(final EntityManager entityManager, final int relationTo,
                                 final int relationId) {
        Query q = entityManager.createQuery("SELECT x FROM VoteSeries x WHERE x.relationTo = :relationTo AND x.relationId = :relationId");
        q.setParameter("relationTo", relationTo);
        q.setParameter("relationId", relationId);
        return (List<VoteSeries>)q.getResultList();
    }

    /**
     * Get a named vote series for a conference
     *
     */

    @SuppressWarnings("unchecked")
    public VoteSeries getByNameForConference(final EntityManager entityManager, final Conference conference, final String id) {
        Query q = entityManager.createQuery("SELECT x FROM VoteSeries x WHERE x.id = :id AND x.relationTo = :relationTo AND x.relationId = :relationId");
        q.setParameter("id", Integer.parseInt(id));
        q.setParameter("relationTo", RELATION_TO_CONFERENCE);
        q.setParameter("relationId", conference.getId());
        List<VoteSeries> series = (List<VoteSeries>)q.getResultList();
        if(series.isEmpty()) {
            return null;
        }
        if(series.size() > 1) {
            throw new RuntimeException("Two vote series for "+id);
        }
        return series.get(0);
    }

    //------------------------ Singleton pattern to fetch this DAO ----------------------------------

    private static final class InstanceHolder {
        private static final VoteSeriesDAO INSTANCE = new VoteSeriesDAO();
    }

    public static VoteSeriesDAO getInstance() {
        return InstanceHolder.INSTANCE;
    }
}
