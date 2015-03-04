package com.conferenceengineer.server.datamodel;

import javax.persistence.EntityManager;

/**
 * Data Accessor Object for ConferencePermission objects
 */
public class ConferencePermissionDAO {

    /**
     * Store the information about a user.
     *
     * @param entityManager The current EntityManager
     * @param conference The Conference this permission relates to
     * @param user The user this permission relates to
     * @param permission The permission.
     */
    public void store(final EntityManager entityManager, final Conference conference, final SystemUser user,
                      final int permission) {
        entityManager.persist(new ConferencePermission(conference, user, permission));
    }
}
