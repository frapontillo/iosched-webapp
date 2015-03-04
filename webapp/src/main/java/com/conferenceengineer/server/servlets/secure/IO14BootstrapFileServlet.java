package com.conferenceengineer.server.servlets.secure;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.exporters.iosched14.BlocksJSON;
import com.conferenceengineer.server.exporters.iosched14.ExpertsJSON;
import com.conferenceengineer.server.exporters.iosched14.HashtagsJSON;
import com.conferenceengineer.server.exporters.iosched14.PartnersJSON;
import com.conferenceengineer.server.exporters.iosched14.RoomsJSON;
import com.conferenceengineer.server.exporters.iosched14.SessionsJSON;
import com.conferenceengineer.server.exporters.iosched14.SpeakersJSON;
import com.conferenceengineer.server.exporters.iosched14.TagsJSON;
import com.conferenceengineer.server.exporters.iosched14.VideosJSON;
import com.conferenceengineer.server.utils.ConferenceUtils;
import com.conferenceengineer.server.utils.EntityManagerWrapperBridge;
import org.json.JSONObject;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet to handle tracks
 */
public class IO14BootstrapFileServlet extends HttpServlet {

    /**
     * Get the JSON in a format suitable for IOSched
     */

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        response.setHeader("Content-Disposition", "attachment; filename=\"bootstrap_data.json\"");
        response.setCharacterEncoding("UTF-8");

        EntityManager em = EntityManagerWrapperBridge.getEntityManager(request);
        try {
            Conference conference = ConferenceUtils.getCurrentConference(request, em);
            if(conference == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            JSONObject jsonRoot = new JSONObject();
            new BlocksJSON(em, conference).addToJSONObject(jsonRoot);
            new ExpertsJSON(em, conference).addToJSONObject(jsonRoot);
            new HashtagsJSON(em, conference).addToJSONObject(jsonRoot);
            new PartnersJSON(em, conference).addToJSONObject(jsonRoot);
            new RoomsJSON(em, conference).addToJSONObject(jsonRoot);
            new SessionsJSON(em, conference).addToJSONObject(jsonRoot);
            new SpeakersJSON(em, conference).addToJSONObject(jsonRoot);
            new TagsJSON(em, conference).addToJSONObject(jsonRoot);
            new VideosJSON(em, conference).addToJSONObject(jsonRoot);

            byte[] utf8EncodedData = jsonRoot.toString().getBytes("UTF-8");
            response.getOutputStream().write( utf8EncodedData );
        } finally {
            em.close();
        }
    }
}
