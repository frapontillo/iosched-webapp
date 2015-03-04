package com.conferenceengineer.server.datamodel.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper for the EntityManagerFactory which re-uses any existing EntityManager and allows the
 * EntityManager to be cleanly closed
 */
public class EntityManagerFactoryWrapper {

    private static final Map<String,String> ENTITY_MANAGER_PROPERTIES = new HashMap<>();
    static {
        // TODO Insert correct database credentials

        ENTITY_MANAGER_PROPERTIES.put("javax.persistence.jdbc.user", "");
        ENTITY_MANAGER_PROPERTIES.put("javax.persistence.jdbc.password", "");
        ENTITY_MANAGER_PROPERTIES.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
        ENTITY_MANAGER_PROPERTIES.put("javax.persistence.jdbc.url", "jdbc:postgresql://localhost:5432/conferenceengineer");
    }


    protected static Map<String,String> sEntityManagerProperties = ENTITY_MANAGER_PROPERTIES;

    /**
     * The EntityManagerFactory for this application.
     */

    private EntityManagerFactory mEntityManagerFactory = null;

    /**
     * Private constructor to enforce singleton pattern
     */

    private EntityManagerFactoryWrapper() {
        super();
    }


    /**
     * Get an EntityManager for use in an application.
     *
     * @return An usable EntityManager
     */

    public synchronized EntityManager getEntityManager() {
        if(mEntityManagerFactory == null) {
            mEntityManagerFactory = Persistence.createEntityManagerFactory("conferenceengineer", sEntityManagerProperties);
        }
        return mEntityManagerFactory.createEntityManager();
    }

    /**
     * Close the factory
     */

    public synchronized void close() {
        if(mEntityManagerFactory != null) {
            mEntityManagerFactory.close();
        }
    }

    /**
     * Singleton getInstance method
     */

    public static EntityManagerFactoryWrapper getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Singleton instance holder
     */

    private static final class InstanceHolder {
        private static final EntityManagerFactoryWrapper INSTANCE = new EntityManagerFactoryWrapper();
    }
}
