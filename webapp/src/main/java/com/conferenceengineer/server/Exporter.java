package com.conferenceengineer.server;

import com.conferenceengineer.server.datamodel.Conference;

public interface Exporter {

    /**
     * Create the export and place it in a string.
     *
     * @param conference The conference to export.
     */

    public String export(final Conference conference);
}
