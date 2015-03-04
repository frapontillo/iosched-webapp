package com.conferenceengineer.server.datamodel;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.List;

public class LastExportDAO {

    private static final int STARTING_SERIAL_NUMBER = 2;

    private static final String GET_BY_NAME_SQL =
            "SELECT x FROM LastExport x WHERE x.conference = :conference AND x.name = :name";

    public void recordExport(final EntityManager entityManager, final Conference conference, String name, final int serialNumber ) {
        LastExport lastExport = getByName(entityManager, conference, name);
        boolean isNewEntry = lastExport == null;
        if(isNewEntry) {
            lastExport = createLastExport(name);
        }

        lastExport.setLastExport(Calendar.getInstance());
        lastExport.setSerialNumber(serialNumber);

        if(isNewEntry) {
            entityManager.persist(lastExport);
        }
    }

    private LastExport createLastExport(final String name) {
        LastExport newObject = new LastExport();
        newObject.setName(name);
        return newObject;
    }

    public LastExport getByName(final EntityManager entityManager, final Conference conference, final String name) {
        TypedQuery<LastExport> q = entityManager.createQuery(GET_BY_NAME_SQL, LastExport.class);
        q.setParameter("conference", conference);
        q.setParameter("name", name);
        q.setMaxResults(1);
        try {
            return q.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    public LastExport getByNameOrCreate(EntityManager em, Conference conference, String name) {
        LastExport lastExport = getByName(em, conference, name);
        if(lastExport != null) {
            return lastExport;
        }

        lastExport = new LastExport();
        lastExport.setName(name);
        lastExport.setConference(conference);
        lastExport.setSerialNumber(STARTING_SERIAL_NUMBER);
        em.persist(lastExport);
        return getByName(em, conference, name);
    }

    private static class InstanceHolder {
        static final LastExportDAO INSTANCE = new LastExportDAO();
    }

    public static LastExportDAO getInstance() {
        return InstanceHolder.INSTANCE;
    }
}
