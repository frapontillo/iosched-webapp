package com.conferenceengineer.server;

import com.conferenceengineer.server.datamodel.SystemUser;
import com.conferenceengineer.server.datamodel.SystemUserDAO;
import com.conferenceengineer.server.security.PasswordResetRequest;
import com.conferenceengineer.server.security.PasswordResetRequestManager;
import com.conferenceengineer.server.utils.EntityManagerWrapperBridge;
import com.conferenceengineer.server.utils.JavaMailUtilsFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Servlet handling registrations
 */
public class ForgottenServlet extends HttpServlet {

    private static final String RESET_REQUEST_BASE = "https://conferenceengineer.com/PasswordReset";

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/forgotten_password.jsp").forward(request, response);
    }

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        String  email = request.getParameter("email");

        if(email == null || email.isEmpty()) {
            request.getSession().setAttribute("error", "Please provide your email address.");
            doGet(request, response);
            return;
        }

        EntityManager em = EntityManagerWrapperBridge.getEntityManager(request);
        try {
            SystemUser user = SystemUserDAO.getInstance().getByEmail(em, email);
            if(user == null) {
                request.getSession().setAttribute("error", "That email address has not been registered.");
                doGet(request, response);
                return;
            }

            em.getTransaction().begin();
            try {
                String secret = request.getParameter("secret");
                PasswordResetRequest resetRequest = PasswordResetRequestManager.create(em, user, secret);
                sendEmailForRequest(resetRequest);
                em.getTransaction().commit();
            } catch(Exception ex) {
                em.getTransaction().rollback();
                throw ex;
            }

            request.getRequestDispatcher("/forgotten_email_sent.jsp").forward(request, response);
        } catch (NoSuchAlgorithmException | MessagingException e) {
            request.getSession().setAttribute("error", "There was a problem sending your password reset email, please try again later.");
            log("Problem creating account", e);
            doGet(request, response);
        } finally {
            em.close();
        }

    }


    private void sendEmailForRequest(final PasswordResetRequest resetRequest)
            throws NoSuchAlgorithmException, UnsupportedEncodingException, MessagingException {
        String resetUrl = buildResetURL(resetRequest);

        Session session = JavaMailUtilsFactory.getJavaMailUtilsInstance().getJavaMailSession();

        StringBuilder messageText = new StringBuilder();
        String secret = resetRequest.getSecret();
        if (secret != null && !secret.isEmpty()) {
            messageText.append("The secret you specified is : ");
            messageText.append(secret);
            messageText.append("\n\n");
        }
        messageText.append("Please visit the following URL to reset your password; \n\n");
        messageText.append(resetUrl);

        Message message = new MimeMessage(session);
        message.setFrom(
                new InternetAddress("support@funkyandroid.com")
        );
        message.setRecipient(
                Message.RecipientType.TO,
                new InternetAddress(resetRequest.getUser().getEmail())
        );
        message.setSubject("Conference Engineer Password Reset");
        message.setText(messageText.toString());
        message.setHeader("X-Mailer", "ConferenceEngineerAutomailer");
        message.setSentDate(new Date());

        Transport.send(message);

        resetRequest.setState(PasswordResetRequest.STATE_EMAIL_SENT);
    }

    private String buildResetURL(final PasswordResetRequest resetRequest) {
        return RESET_REQUEST_BASE
              + "?i=" + resetRequest.getId()
              + "&u=" + resetRequest.getUser().getId()
              + "&t=" + resetRequest.getTimestamp().getTimeInMillis();
    }
}
