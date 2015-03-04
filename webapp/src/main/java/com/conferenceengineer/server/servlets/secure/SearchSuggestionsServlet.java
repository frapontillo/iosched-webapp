package com.conferenceengineer.server.servlets.secure;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.exporters.iosched13.SearchSuggestionsJSON;

/**
 * Servlet to handle tracks
 */
public class SearchSuggestionsServlet extends IOSched13ExportServlet {

    private static final String EXPORT_FILENAME = "search_suggest.json";

    @Override
    protected String getExport(final Conference conference) {
        return SearchSuggestionsJSON.export(conference);
    }

    @Override
    protected String getFilename() {
        return EXPORT_FILENAME;
    }
}
