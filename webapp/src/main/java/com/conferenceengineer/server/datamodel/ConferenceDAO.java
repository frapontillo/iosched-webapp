package com.conferenceengineer.server.datamodel;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Data Accessor Object for Conference objects
 */
public class ConferenceDAO {

    /**
     * Store the information about the authentication credentials for an external authenticator.
     *
     * @param entityManager The EntityManager currently in user
     * @param name The name of the conference.
     */
    public Conference create(final EntityManager entityManager, final String name) {
        Conference conference = new Conference(name);
        entityManager.persist(conference);
        return conference;
    }

    /**
     * Get the details about a conference in a series
     *
     * @param entityManager The currently active EntityManager
     * @param series The series the conference is in.
     * @param name The name of the conference to look for.
     */

    @SuppressWarnings("unchecked")
    public Conference getByName(final EntityManager entityManager, final Series series, final String name) {
        Query q = entityManager.createQuery("SELECT x FROM Conference x WHERE x.name = :name AND x.series = :series");
        q.setParameter("series", series);
        q.setParameter("name", name);
        List<Conference> conferences = (List<Conference>)q.getResultList();
        if(conferences.size() > 1) {
            throw new RuntimeException("Multiple series found for "+name);
        }
        if(conferences.isEmpty()) {
            return null;
        }
        return conferences.get(0);
    }

    //------------------------ Singleton pattern to fetch this DAO ----------------------------------

    private static final class InstanceHolder {
        private static final ConferenceDAO INSTANCE = new ConferenceDAO();
    }

    public static ConferenceDAO getInstance() {
        return InstanceHolder.INSTANCE;
    }
}
