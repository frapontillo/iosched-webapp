package com.conferenceengineer.server.datamodel;

import javax.persistence.EntityManager;

/**
 * Data Accessor Object for SystemUsers
 */
public class SeriesConfigurationDAO {

    /**
     * Store the information about a conference series related configuration option.
     *
     * @param entityManager The EntityManager currently in user
     * @param series The conference Series this configuration option relates to.
     * @param propertyName The name of the property for the configuration option.
     * @param propertyValue The value of the property for this configuration option.
     */
    public void store(final EntityManager entityManager, final Series series, final String propertyName,
                      final String propertyValue) {
        entityManager.persist(new SeriesConfiguration(series, propertyName, propertyValue));
    }
}
