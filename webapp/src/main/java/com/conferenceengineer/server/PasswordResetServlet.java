package com.conferenceengineer.server;

import com.conferenceengineer.server.datamodel.SystemUser;
import com.conferenceengineer.server.datamodel.SystemUserDAO;
import com.conferenceengineer.server.security.PasswordResetRequest;
import com.conferenceengineer.server.security.PasswordResetRequestManager;
import com.conferenceengineer.server.utils.EntityManagerWrapperBridge;
import com.conferenceengineer.server.utils.PasswordGenerator;
import com.conferenceengineer.server.utils.ServletUtils;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class PasswordResetServlet extends HttpServlet {

    private static final long RESET_REQUEST_EXPIRY_TIMEOUT = TimeUnit.DAYS.toMillis(1L);

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        String  requestIdString = request.getParameter("i"),
                userIdString = request.getParameter("u"),
                timestampString = request.getParameter("t");

        if(requestIdString == null   || userIdString == null   || timestampString == null
        || requestIdString.isEmpty() || userIdString.isEmpty() || timestampString.isEmpty() ) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int requestId, userId;
        long timestamp;
        try {
            requestId = Integer.parseInt(requestIdString);
            userId = Integer.parseInt(userIdString);
            timestamp = Long.parseLong(timestampString);
        } catch (NumberFormatException e) {
            log("Problem converting a reset request "+request.getRequestURI(), e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        EntityManager em = EntityManagerWrapperBridge.getEntityManager(request);
        try {
            PasswordResetRequest resetRequest =
                    PasswordResetRequestManager.getPasswordResetRequest(em, requestId);

            if (resetRequest.getUser().getId() != userId
                    || resetRequest.getTimestamp().getTimeInMillis() != timestamp) {
                log("Encountered data mismatch in reset request " + request.getRequestURI());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            if (timestamp + RESET_REQUEST_EXPIRY_TIMEOUT < Calendar.getInstance().getTimeInMillis()) {
                log("Encountered expired reset request " + request.getRequestURI());
                response.sendError(HttpServletResponse.SC_GONE);
                return;
            }

            SystemUser user = SystemUserDAO.getInstance().get(em, resetRequest.getUser().getId());

            em.getTransaction().begin();
            try {
                PasswordGenerator.getInstance().randomiseUsersPassword(em, user);
                resetRequest.setState(PasswordResetRequest.STATE_PERFORMED);
                em.getTransaction().commit();
            } catch(Exception ex) {
                em.getTransaction().rollback();
                throw ex;
            }

            request.getSession().setAttribute("message", "Your new password will be emailed to you.");
            ServletUtils.sendUserToPostLoginPage(request, response, user);
        } catch (MessagingException e) {
            log("Problem sending password reset email", e);
            // TODO: Handle Email failures more cleanly
            throw new ServletException(e);
        } catch (NoSuchAlgorithmException e) {
            log("Problem resetting password", e);
            throw new ServletException(e);
        } finally {
            em.close();
        }
    }
}
