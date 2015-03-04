package com.conferenceengineer.server.servlets.secure;

import com.conferenceengineer.server.datamodel.*;
import com.conferenceengineer.server.utils.EntityManagerWrapperBridge;
import com.conferenceengineer.server.utils.LoginUtils;
import com.conferenceengineer.server.utils.ServletUtils;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Servlet handling user update information
 */
public class UserServlet extends HttpServlet {

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws IOException, ServletException {
        EntityManager em = EntityManagerWrapperBridge.getEntityManager(request);
        try {
            SystemUser user = LoginUtils.getInstance().getUserFromCookie(request, em);
            String id = request.getParameter("id");
            if(user.getId() != Integer.parseInt(id)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String passwords[] = new String[2];
            passwords[0] = request.getParameter("password1");
            passwords[1] = request.getParameter("password2");
            if((passwords[0] != null && passwords[1] != null)) {
                if(passwords[0].equals(passwords[1])) {
                    List<UserAuthenticationInformation> authenticators =
                            UserAuthenticationInformationDAO.getInstance().getAuthenticators(em, user);
                    for(UserAuthenticationInformation verifier : authenticators) {
                        if(verifier.getAuthenticatorType() == UserAuthenticationInformationDAO.AUTHENTICATOR_INTERNAL) {
                            em.getTransaction().begin();
                            try {
                                verifier.setInformation(
                                    UserAuthenticationInformationDAO.
                                            getInstance().
                                            createInternalAuthenticatorString(passwords[0])
                                    );
                                em.getTransaction().commit();
                                request.getSession().setAttribute("message", "Passwords Updated");
                            } catch (NoSuchAlgorithmException e) {
                                log("Unable to create new password string");
                                em.getTransaction().rollback();
                            }
                            break;
                        }
                    }
                } else {
                    request.getSession().setAttribute("error", "Password not changed, the supplied passwords did not match");
                }
            }
        } finally {
            em.close();
        }

        ServletUtils.redirectTo(request, response, "/secure/Admin");
    }

}
