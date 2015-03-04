package com.conferenceengineer.server.servletfilters;

import com.conferenceengineer.server.utils.JavaMailUtilsFactory;
import com.conferenceengineer.server.utils.ServletUtils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ServletFilter to report uncaught exceptions
 */
public class ErrorReporter implements Filter {

    private static final String RECIPIENT_PARAMETER_NAME = "recipient";

    private static final String EMAIL_SUBJECT = "Conference Engineer Uncaught Exception";

    private String mRecipient;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        mRecipient = filterConfig.getInitParameter(RECIPIENT_PARAMETER_NAME);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Throwable t) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Uncaught Exception", t);
            new Thread(new ExceptionReporter(t)).start();
            ServletUtils.redirectTo((HttpServletRequest)request, (HttpServletResponse)response, "/error.jsp");
        }
    }

    @Override
    public void destroy() {

    }

    private class ExceptionReporter implements Runnable {

        private String mMessage;

        ExceptionReporter(final Throwable exception) {
            StringBuilder messageBuilder = new StringBuilder(1024);

            Throwable currentException = exception;
            while(currentException != null) {
                messageBuilder.append(currentException.toString());
                messageBuilder.append("\r\n");
                addStackTraceToMessageBuilder(messageBuilder, currentException);
                currentException = currentException.getCause();
            }
            mMessage = messageBuilder.toString();
        }

        private void addStackTraceToMessageBuilder(StringBuilder messageBuilder, Throwable t) {
            for(StackTraceElement stackTraceElement : t.getStackTrace()) {
                messageBuilder.append(stackTraceElement.toString());
                messageBuilder.append("\r\n");
            }
            messageBuilder.append("\r\n");
        }

        public void run() {
            Session session = JavaMailUtilsFactory.getJavaMailUtilsInstance().getJavaMailSession();

            try {
                Message message = new MimeMessage(session);
                message.setFrom(
                        new InternetAddress("support@funkyandroid.com")
                );
                message.setRecipient(
                        Message.RecipientType.TO,
                        new InternetAddress(mRecipient)
                );
                message.setSubject(EMAIL_SUBJECT);
                message.setText(mMessage);
                message.setHeader("X-Mailer", "ConferenceEngineerAutomailer");
                message.setSentDate(new Date());

                Transport.send(message);
            } catch(MessagingException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Unable to report uncaught exception", e);
            }

        }

    }
}
