package com.conferenceengineer.server.exporters;

import com.conferenceengineer.server.cdn.ContentDeliveryNetwork;
import com.conferenceengineer.server.cdn.ContentDeliveryNetworkFactory;
import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.utils.EntityManagerFactoryWrapper;
import com.conferenceengineer.server.exporters.html.ScheduleExporter;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Publisher which pushes the conference data to the Rackspace endpoint for this conference
 */
public class SchedulePublisher extends ExportPublisher {

    private static final String UPDATES_CONTAINER = "schedules.conferenceengineer.com";

    private Conference mConference;

    public SchedulePublisher(final Conference conference) {
        mConference = conference;
    }

    public void run() {
        EntityManager em = EntityManagerFactoryWrapper.getInstance().getEntityManager();
        try(ContentDeliveryNetwork cdn = ContentDeliveryNetworkFactory.getInstance(UPDATES_CONTAINER)) {
            exportData(em, cdn);
        } finally {
            em.close();
        }
    }

    private void exportData(final EntityManager em, final ContentDeliveryNetwork cdn) {
        EntityTransaction transaction = em.getTransaction();

        String htmlSchedule = new ScheduleExporter(mConference).createExport();
        try {
            cdn.putData(Integer.toString(mConference.getId())+".html", htmlSchedule, "text/html");
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Problem publishing manifest", e);
            setLastIOException(e);
            return;
        }

        setComplete();
    }

}
