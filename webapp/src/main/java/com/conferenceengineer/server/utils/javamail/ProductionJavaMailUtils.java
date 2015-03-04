package com.conferenceengineer.server.utils.javamail;

import com.conferenceengineer.server.utils.JavaMailUtils;

import javax.mail.Session;
import java.util.Properties;

public class ProductionJavaMailUtils implements JavaMailUtils {
    @Override
    public Session getJavaMailSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "localhost");
        return Session.getDefaultInstance(props);
    }
}
