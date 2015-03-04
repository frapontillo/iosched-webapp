package com.conferenceengineer.server.utils.javamail;

import com.conferenceengineer.server.utils.JavaMailUtils;

import javax.mail.Session;
import java.util.Properties;

public class DevelopmentJavaMailUtils implements JavaMailUtils {
    @Override
    public Session getJavaMailSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "mailhost.zen.co.uk");
        return Session.getDefaultInstance(props);
    }
}
