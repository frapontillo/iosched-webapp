package com.conferenceengineer.server.utils.javamail;

import com.conferenceengineer.server.utils.JavaMailUtils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class ProductionJavaMailUtils implements JavaMailUtils {
    private static final String SMTP_AUTH_USER ="" ;
    private static final String SMTP_AUTH_PWD = "";

    @Override
    public Session getJavaMailSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.sendgrid.net");
        props.put("mail.smtp.port", "2525");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.auth", "true");
        Authenticator auth = new SMTPAuthenticator();
        return Session.getDefaultInstance(props, auth);
    }

    // Authenticates to SendGrid
    private class SMTPAuthenticator extends javax.mail.Authenticator {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username = SMTP_AUTH_USER;
            String password = SMTP_AUTH_PWD;
            return new PasswordAuthentication(username, password);
        }
    }
}
