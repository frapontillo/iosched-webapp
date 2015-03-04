package com.conferenceengineer.server.connectors.iosched14.gcm;

import java.util.Map;

/**
 * Interface for classes which represent messages which can be sent
 * to the IOSched14 GCM application.
 */
public interface Message {

    public String getEndpoint();
    public Map<String,String> getParameters();
}
