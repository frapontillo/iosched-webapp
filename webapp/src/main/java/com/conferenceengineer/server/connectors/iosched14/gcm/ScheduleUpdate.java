package com.conferenceengineer.server.connectors.iosched14.gcm;

import java.util.HashMap;
import java.util.Map;

/**
 * Message to get clients to update their schedule.
 */
public class ScheduleUpdate extends AbstractMessage {

    private static final int SYNC_JITTER = 600000;
    private static final Map<String, String> REQUEST_PARAMETERS = new HashMap<String,String>();
    static {
        REQUEST_PARAMETERS.put("key", DCUK14_ADMIN_KEY);
        REQUEST_PARAMETERS.put("sync_jitter", Integer.toString(SYNC_JITTER));
    }

    @Override
    public String getEndpoint() {
        return DCUK14_MESSAGE_ENDPOINT_BASE + "/global/sync_schedule";
    }

    @Override
    public Map<String,String> getParameters() {
        return REQUEST_PARAMETERS;
    }

    private static final class InstanceHolder {
        static final ScheduleUpdate INSTANCE = new ScheduleUpdate();
    }

    public static ScheduleUpdate getInstance() {
        return InstanceHolder.INSTANCE;
    }
}
