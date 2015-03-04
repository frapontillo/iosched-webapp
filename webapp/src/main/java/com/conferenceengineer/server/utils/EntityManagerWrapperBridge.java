package com.conferenceengineer.server.utils;

import com.conferenceengineer.server.EntityWrapperManagerFilter;
import com.conferenceengineer.server.datamodel.utils.EntityManagerFactoryWrapper;

import javax.persistence.EntityManager;
import javax.servlet.ServletRequest;

/**
 * Class which handles extracting the EntityManager from the EntityManagerFactoryWrapper for a request.
 */
public final class EntityManagerWrapperBridge {

    /**
     * Private constructor to prevent instantiation.
     */

    private EntityManagerWrapperBridge() {
        super();
    }

    /**
     * Get an EntityManager from a request.
     *
     */

    public static EntityManager getEntityManager(final ServletRequest request) {
        EntityManagerFactoryWrapper emfw =
                (EntityManagerFactoryWrapper) request.getAttribute(EntityWrapperManagerFilter.EMFW_ATTRIBUTE);
        return emfw.getEntityManager();
    }

}
