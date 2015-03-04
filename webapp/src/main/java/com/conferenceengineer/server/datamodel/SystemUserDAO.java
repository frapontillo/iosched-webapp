package com.conferenceengineer.server.datamodel;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Data Accessor Object for SystemUsers
 */
public final class SystemUserDAO {

    /**
     * The flag to denote the user is an admin
     */

    public static final int FLAG_IS_ADMIN = 0x01 << 0;

    /**
     * Private constructor to avoid external instantiation
     */

    private SystemUserDAO() {
        super();
    }

    /**
     * Store the information about a user.
     *
     * @param entityManager The EntityManager currently in use.
     * @param email The email address for the SystemUser.
     */
    public void store(final EntityManager entityManager, final String email) {
        entityManager.persist(new SystemUser(email));
    }

    /**
     * Get the user with a specific ID.
     */

    public SystemUser get(final EntityManager entityManager, final int id) {
        return entityManager.find(SystemUser.class, id);
    }

    /**
     * Get the details about a user from their email address.
     *
     * @param entityManager The currently active EntityManager
     * @param email The Email Address of the person to get the information for.
     */

    @SuppressWarnings("unchecked")
    public SystemUser getByEmail(final EntityManager entityManager, final String email) {
        Query q = entityManager.createQuery("SELECT x FROM SystemUser x WHERE x.email = :email");
        q.setParameter("email", email);
        List<SystemUser> users = (List<SystemUser>)q.getResultList();
        if(users.size() > 1) {
            throw new RuntimeException("Multiple users found for "+email);
        }
        if(users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }


    //------------------------ Singleton pattery to fetch this DAO ----------------------------------

    private static final class InstanceHolder {
        private static final SystemUserDAO INSTANCE = new SystemUserDAO();
    }

    public static SystemUserDAO getInstance() {
        return InstanceHolder.INSTANCE;
    }

}
