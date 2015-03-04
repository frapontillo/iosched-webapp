package com.conferenceengineer.server.servlets.secure;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.exporters.iosched13.TrackSessionsJSON;

/**
 * Servlet to handle tracks
 */
public class TrackSessionsServlet extends IOSched13ExportServlet {

    private static final String EXPORT_FILENAME = "session_tracks.json";

    @Override
    protected String getExport(final Conference conference) {
        return TrackSessionsJSON.export(conference);
    }

    @Override
    protected String getFilename() {
        return EXPORT_FILENAME;
    }

}
