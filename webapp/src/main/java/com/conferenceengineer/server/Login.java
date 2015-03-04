package com.conferenceengineer.server;

import com.conferenceengineer.server.datamodel.SystemUser;
import com.conferenceengineer.server.datamodel.SystemUserDAO;
import com.conferenceengineer.server.datamodel.UserAuthenticationInformation;
import com.conferenceengineer.server.datamodel.UserAuthenticationInformationDAO;
import com.conferenceengineer.server.utils.EntityManagerWrapperBridge;
import com.conferenceengineer.server.utils.LoginUtils;
import com.conferenceengineer.server.utils.ServletUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class Login extends HttpServlet {

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        EntityManager em = EntityManagerWrapperBridge.getEntityManager(request);
        try {
            EntityTransaction transaction = em.getTransaction();
            try {
                transaction.begin();

                SystemUser user = getUserFromRequest(em, request);

                ServletUtils.sendUserToPostLoginPage(request, response, user);

                transaction.commit();
            } catch(InvalidUserDetailsException e) {
                reportLoginError(request, response);
            } finally {
                if(transaction.isActive()) {
                    transaction.rollback();
                }
            }
        } finally {
            em.close();
        }
    }

    private void reportLoginError(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ServletException {
        request.getSession().setAttribute("error", "The login details were incorrect");
        ServletUtils.redirectToIndex(request, response);
    }

    private SystemUser getUserFromRequest(final EntityManager em, final HttpServletRequest request) {
        SystemUser user = getUserForSpecifiedUsername(em, request);

        if(!isPasswordGivenCorrect(em, request, user)) {
            throw new InvalidUserDetailsException();
        }

        return user;
    }

    private SystemUser getUserForSpecifiedUsername(EntityManager em, HttpServletRequest request) {
        String name = request.getParameter("username");
        if (name == null || name.isEmpty()) {
            throw new InvalidUserDetailsException();
        }

        SystemUser user = SystemUserDAO.getInstance().getByEmail(em, name);
        if (user == null) {
            throw new InvalidUserDetailsException();
        }

        return user;
    }

    private boolean isPasswordGivenCorrect(EntityManager em, HttpServletRequest request, SystemUser user) {
        String password = request.getParameter("password");
        if (password == null || password.isEmpty()) {
            throw new InvalidUserDetailsException();
        }

        List<UserAuthenticationInformation> authenticators =
                UserAuthenticationInformationDAO.getInstance().getAuthenticators(em, user);
        if (authenticators == null || authenticators.isEmpty()) {
            throw new InvalidUserDetailsException();
        }

        LoginUtils loginUtils = LoginUtils.getInstance();
        for (UserAuthenticationInformation authenticator : authenticators) {
            if (authenticator.getAuthenticatorType() == UserAuthenticationInformationDAO.AUTHENTICATOR_INTERNAL
            &&  loginUtils.isUserValid(user.getEmail(), password, authenticator)) {
                return true;
            }
        }

        return false;
    }


    private static class InvalidUserDetailsException extends RuntimeException {}
}
