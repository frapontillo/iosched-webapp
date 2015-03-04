package com.conferenceengineer.server.connectors.iosched14.gcm;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

/**
 * Connector to the IOSched14 GCM application
 */
public class IOSched14Connector {

    /**
     * Issue a POST request to the server.
     *
     */
    public void send(Message message) throws IOException {
        URL url;
        try {
            url = new URL(message.getEndpoint());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + message.getEndpoint());
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator =
                message.getParameters().entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Map.Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setChunkedStreamingMode(0);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestProperty("Content-Length",
                    Integer.toString(body.length()));
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(body.getBytes(Charset.forName("UTF-8")));
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
                throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }



    private static final class InstanceHolder {
        final static IOSched14Connector INSTANCE = new IOSched14Connector();
    }

    public static IOSched14Connector getInstance() {
        return InstanceHolder.INSTANCE;
    }
}
