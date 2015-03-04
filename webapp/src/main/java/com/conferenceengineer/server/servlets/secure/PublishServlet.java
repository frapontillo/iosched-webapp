package com.conferenceengineer.server.servlets.secure;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.ConferenceMetadata;
import com.conferenceengineer.server.datamodel.PublicationEndpoint;
import com.conferenceengineer.server.exporters.ExportEndpointManager;
import com.conferenceengineer.server.exporters.ExportPublisher;
import com.conferenceengineer.server.exporters.IOSched14Publisher;
import com.conferenceengineer.server.utils.ConferenceUtils;
import com.conferenceengineer.server.utils.EntityManagerWrapperBridge;
import com.conferenceengineer.server.utils.ServletUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Publish the conference data to the server.
 */

public class PublishServlet extends HttpServlet {

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws IOException, ServletException {

        EntityManager em = EntityManagerWrapperBridge.getEntityManager(request);
        try {
            Conference conference = ConferenceUtils.getCurrentConference(request, em);
            if(conference == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            checkConferenceHasIOSched14Endpoint(em, conference);

            ExportEndpointManager exportEndpointManager = new ExportEndpointManager(conference);

            List<ExportPublisher> publishers = exportEndpointManager.runPublishers();

            StringBuilder errorsBuilder = null;
            boolean publishersCompleted = true;
            for(ExportPublisher publisher : publishers) {
                if(publisher.hasExportCompletedSuccessfully()) {
                    continue;
                }

                publishersCompleted = false;
                IOException exception = publisher.getLastIOException();
                if(exception != null) {
                    errorsBuilder = addExceptionToErrors(errorsBuilder, exception);
                }
            }

            if(publishersCompleted) {
                onPublishSuccessfullyCompleted(em,conference);
                request.getSession().setAttribute("message", "The updates have been published");
            } else {
                String errorMessage = errorsBuilder == null ? "" : errorsBuilder.toString();
                request.getSession().setAttribute("error", "Problem during publishing : "+errorMessage);
            }
        } catch(IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Problem during publish", e);
            request.getSession().setAttribute("error", "Problem during publish : "+e.getMessage());
        } finally {
            em.close();
        }

        ServletUtils.redirectTo(request, response, "/secure/Admin");
    }

    private void checkConferenceHasIOSched14Endpoint(EntityManager em, Conference conference) {
        Map<Integer, PublicationEndpoint> publicationEndpointList = conference.getPublicationEndpoints();
        if(publicationEndpointList != null && !publicationEndpointList.isEmpty()) {
            return;
        }

        createIOSched14Endpoint(em, conference);
    }

    private void createIOSched14Endpoint(EntityManager em, Conference conference) {
        IOSched14Publisher ioSched14Publisher = new IOSched14Publisher(conference);

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        PublicationEndpoint endpoint = new PublicationEndpoint();
        endpoint.setConference(conference);
        endpoint.setType(PublicationEndpoint.TYPE_IOSCHED14);
        endpoint.setUrl(ioSched14Publisher.getManifestUrl());
        em.persist(endpoint);

        conference.getPublicationEndpoints().put(endpoint.getId(), endpoint);
        transaction.commit();
    }

    private StringBuilder addExceptionToErrors(StringBuilder messageBuilder, final Exception e) {
        if(messageBuilder == null) {
            messageBuilder = new StringBuilder(e.getMessage().length());
        } else {
            messageBuilder.append(", ");
        }
        messageBuilder.append(e.getMessage());
        return messageBuilder;
    }

    private void onPublishSuccessfullyCompleted(EntityManager em, Conference conference) {
        em.getTransaction().begin();
        ConferenceMetadata metadata = conference.getMetadata();
        if (metadata == null) {
            metadata = new ConferenceMetadata();
            metadata.setConference(conference);
            em.persist(metadata);
            conference.setMetadata(metadata);
        }
        metadata.setLastPublished(Calendar.getInstance());
        em.getTransaction().commit();
    }
}
