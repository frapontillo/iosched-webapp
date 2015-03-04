package com.conferenceengineer.server.datamodel;

import javax.persistence.EntityManager;

/**
 * Data Accessor Object for LocationLink Objects
 */
public class LocationLinkDAO {

    /**
     * Store the information about a link relevant to a location.
     *
     * @param entityManager The currently active EntityManager
     * @param location The location the link is relevant to.
     * @param name The name of the location link.
     * @param link The link.
     */
    public void store(final EntityManager entityManager, final Location location, final String name, final String link ) {
        entityManager.persist(new LocationLink(location, name, link));
    }

    /**
     * Fetch and object by it's ID
     *
     * @param entityManager The currently active EntityManager
     * @param id The ID of the object to get
     */

    public LocationLink getById(final EntityManager entityManager, final int id) {
        return entityManager.find(LocationLink.class, id);
    }
}
