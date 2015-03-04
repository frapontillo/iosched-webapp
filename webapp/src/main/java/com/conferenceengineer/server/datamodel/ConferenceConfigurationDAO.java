package com.conferenceengineer.server.datamodel;

import javax.persistence.EntityManager;

/**
 * Data Accessor Object for ConferenceConfiguration objects.
 */
public class ConferenceConfigurationDAO {

    /**
     * Store the information about a conference series related configuration option.
     *
     * @param entityManager The EntityManager currently in user
     * @param conference The conference this configuration option relates to.
     * @param propertyName The name of the property for the configuration option.
     * @param propertyValue The value of the property for this configuration option.
     */
    public void store(final EntityManager entityManager, final Conference conference, final String propertyName,
                      final String propertyValue) {
        entityManager.persist(new ConferenceConfiguration(conference, propertyName, propertyValue));
    }
}
