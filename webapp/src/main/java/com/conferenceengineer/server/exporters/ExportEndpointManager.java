package com.conferenceengineer.server.exporters;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.PublicationEndpoint;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExportEndpointManager {

    private static final Map<Integer, Class<? extends ExportPublisher>> EXPORTER_MAP = new HashMap<>();
    static {
        EXPORTER_MAP.put(PublicationEndpoint.TYPE_IOSCHED13, IOSched13Publisher.class);
        EXPORTER_MAP.put(PublicationEndpoint.TYPE_IOSCHED14, IOSched14Publisher.class);
    }

    private static final List<ExportPublisher> EMPTY_PUBLISHER_LIST = new ArrayList<>();

    private final Conference mConference;

    public ExportEndpointManager(final Conference conference) {
        mConference = conference;
    }

    public List<ExportPublisher> runPublishers()
            throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        List<ExportPublisher> publishers = getPublishers();
        ExecutorService executorService = Executors.newCachedThreadPool();
        for(ExportPublisher publisher : publishers) {
            executorService.submit(publisher);
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(2L, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Publishers were interrupted", e);
        }
        return publishers;
    }

    private List<ExportPublisher> getPublishers()
            throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Map<Integer,PublicationEndpoint> publicationEndpoints = mConference.getPublicationEndpoints();
        if(publicationEndpoints == null || publicationEndpoints.isEmpty()) {
            return EMPTY_PUBLISHER_LIST;
        }

        List<ExportPublisher> publishers = new ArrayList<>(publicationEndpoints.size());
        for(PublicationEndpoint endpoint : publicationEndpoints.values()) {
            addExportPublisherToList(endpoint, publishers);
        }

        return publishers;
    }

    private void addExportPublisherToList(PublicationEndpoint endpoint, List<ExportPublisher> publishers)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<? extends ExportPublisher> publisherClass = EXPORTER_MAP.get(endpoint.getType());
        Constructor<? extends ExportPublisher> constructor = publisherClass.getConstructor(Conference.class);
        publishers.add(constructor.newInstance(mConference));
    }
}
