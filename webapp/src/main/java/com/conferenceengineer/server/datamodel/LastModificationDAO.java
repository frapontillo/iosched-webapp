package com.conferenceengineer.server.datamodel;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Calendar;

public class LastModificationDAO {

    private static final String GET_BY_NAME_SQL =
            "SELECT x FROM LastModification x WHERE x.conference = :conference AND x.name = :name";

    public void recordUpdate(final EntityManager entityManager, final Conference conference, String name ) {
        LastModification lastModification = getByName(entityManager, conference, name);
        if(lastModification != null) {
            lastModification.setLastModification(Calendar.getInstance());
            return;
        }

        lastModification = new LastModification();
        lastModification.setName(name);
        lastModification.setConference(conference);
        lastModification.setLastModification(Calendar.getInstance());
        entityManager.persist(lastModification);
    }

    private LastModification createLastModification(final Conference conference, final String name) {
        LastModification lastModification = new LastModification();
        lastModification.setConference(conference);
        lastModification.setName(name);
        return lastModification;
    }

    public LastModification getByName(final EntityManager entityManager, final Conference conference, final String name) {
        TypedQuery<LastModification> q = entityManager.createQuery(GET_BY_NAME_SQL, LastModification.class);
        q.setParameter("conference", conference);
        q.setParameter("name", name);
        q.setMaxResults(1);
        try {
            return q.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    private static class InstanceHolder {
        static final LastModificationDAO INSTANCE = new LastModificationDAO();
    }

    public static LastModificationDAO getInstance() {
        return InstanceHolder.INSTANCE;
    }
}
