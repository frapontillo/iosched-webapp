package com.conferenceengineer.server.datamodel;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Data Accessor Object for Series
 */
public class SeriesDAO {

    /**
     * Private constructor to prevent instantiation
     */

    private SeriesDAO() {
        super();
    }

    /**
     * Store the information about a conference series.
     *
     * @param entityManager The currently active EntityManager
     * @param name The name of the conference series.
     */
    public Series store(final EntityManager entityManager, final String name ) {
        Series newSeries = new Series(name);
        entityManager.persist(newSeries);
        return newSeries;
    }

    /**
     * Fetch and object by it's ID
     *
     * @param entityManager The currently active EntityManager
     * @param id The ID of the object to get
     */

    public Series getById(final EntityManager entityManager, final int id) {
        return entityManager.find(Series.class, id);
    }

    /**
     * Get the details about a series from the name
     *
     * @param entityManager The currently active EntityManager
     * @param name The name of the conference to look for.
     */

    @SuppressWarnings("unchecked")
    public Series getByName(final EntityManager entityManager, final String name) {
        Query q = entityManager.createQuery("SELECT x FROM Series x WHERE x.name = :name");
        q.setParameter("name", name);
        List<Series> serieses = (List<Series>)q.getResultList();
        if(serieses.size() > 1) {
            throw new RuntimeException("Multiple series found for "+name);
        }
        if(serieses.isEmpty()) {
            return null;
        }
        return serieses.get(0);
    }

    //------------------------ Singleton pattery to fetch this DAO ----------------------------------

    private static final class InstanceHolder {
        private static final SeriesDAO INSTANCE = new SeriesDAO();
    }

    public static SeriesDAO getInstance() {
        return InstanceHolder.INSTANCE;
    }
}
