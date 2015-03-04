package com.conferenceengineer.server;

import com.conferenceengineer.server.datamodel.SystemUser;
import com.conferenceengineer.server.datamodel.SystemUserDAO;
import com.conferenceengineer.server.utils.EntityManagerWrapperBridge;
import com.conferenceengineer.server.utils.PasswordGenerator;
import com.conferenceengineer.server.utils.ServletUtils;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class RegisterServlet extends HttpServlet {

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        String  name = request.getParameter("name"),
                email = request.getParameter("email");

        if(name == null || name.isEmpty() || email == null || email.isEmpty()) {
            handleError(request, response, "Please fill in all the registration fields.");
            return;
        }

        EntityManager em = EntityManagerWrapperBridge.getEntityManager(request);
        try {
            if(SystemUserDAO.getInstance().getByEmail(em, email) != null) {
                handleError(request, response, "That email address is already registered.");
                return;
            }

            createUser(em, name, email);

            request.getRequestDispatcher("/register_thanks.jsp").forward(request, response);
        } catch (NoSuchAlgorithmException | MessagingException e) {
            log("Problem creating account", e);
            handleError(request, response, "There was a problem creating your account, please try again later.");
        } finally {
            em.close();
        }
    }

    private void createUser(final EntityManager em, final String name, final String email)
            throws NoSuchAlgorithmException, UnsupportedEncodingException, MessagingException {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            SystemUser user = new SystemUser();
            user.setEmail(email);
            user.setHumanName(name);

            em.persist(user);

            PasswordGenerator.getInstance().createRandomPasswordForUser(em, user);
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    private void handleError(final HttpServletRequest request, final HttpServletResponse response,
                             final String message)
            throws IOException {
        ServletUtils.storeErrorInSession(request, message);
        ServletUtils.redirectTo(request, response, "/Register");
    }
}
