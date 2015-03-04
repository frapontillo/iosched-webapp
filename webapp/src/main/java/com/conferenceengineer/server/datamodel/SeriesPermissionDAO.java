package com.conferenceengineer.server.datamodel;

import javax.persistence.EntityManager;

/**
 * Data Accessor Object for SeriesPermission objects
 */
public class SeriesPermissionDAO {

    /**
     * Permission indicating a user is an admin of the given series
     */

    public static final int PERMISSION_IS_ADMIN = 0x01 << 0;

    /**
     * Store the information about a user.
     *
     * @param entityManager The current EntityManager
     * @param series The conference Series this permission relates to
     * @param user The user this permission relates to
     * @param permission The permission.
     */
    public void store(final EntityManager entityManager, final Series series, final SystemUser user,
                      final int permission) {
        entityManager.persist(new SeriesPermission(series, user, permission));
    }
}
