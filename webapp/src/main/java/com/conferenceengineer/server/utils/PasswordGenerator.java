package com.conferenceengineer.server.utils;

import com.conferenceengineer.server.datamodel.SystemUser;
import com.conferenceengineer.server.datamodel.UserAuthenticationInformation;
import com.conferenceengineer.server.datamodel.UserAuthenticationInformationDAO;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

/**
 * Password Generator
 */
public final class PasswordGenerator {

    /**
     * The characters to use in the generation of the password.
     */

    private static final String CANDIDATE_CHARACTERS =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!+-=#/";


    private static final int CANDIDATE_CHARACTES_LENGTH = CANDIDATE_CHARACTERS.length();

    /**
     * Private constructor to stop instantiation.
     */

    private PasswordGenerator() {
        super();
    }

    /**
     * Generate a new password.
     */

    public String generatePassword()
            throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        int length = 8+sr.nextInt(5);
        StringBuilder password = new StringBuilder(length);
        for(int i = 0 ; i < length; i++) {
            password.append(CANDIDATE_CHARACTERS.charAt(sr.nextInt(CANDIDATE_CHARACTES_LENGTH)));
        }
        return password.toString();
    }

    public void randomiseUsersPassword(final EntityManager em, SystemUser user)
            throws NoSuchAlgorithmException, UnsupportedEncodingException, MessagingException {
        String password = generatePassword();

        List<UserAuthenticationInformation> authenticators =
                UserAuthenticationInformationDAO.getInstance().getAuthenticators(em, user);
        if(authenticators == null || authenticators.isEmpty()) {
            throw new RuntimeException("Can not reset the password of a user who isn't authenticated by the system");
        }

        UserAuthenticationInformationDAO uaiDAO = UserAuthenticationInformationDAO.getInstance();

        for(UserAuthenticationInformation authInfo : authenticators) {
            if (authInfo.getAuthenticatorType() != UserAuthenticationInformationDAO.AUTHENTICATOR_INTERNAL) {
                continue;
            }

            authInfo.setInformation(uaiDAO.createInternalAuthenticatorString(password));
        }

        sendPasswordEmail(user, password);
    }

    public void createRandomPasswordForUser(final EntityManager em, SystemUser user)
            throws NoSuchAlgorithmException, UnsupportedEncodingException, MessagingException {
        String password = generatePassword();

        UserAuthenticationInformationDAO.
                getInstance().
                createInternalAuthenticationEntry(em, user, password);

        sendPasswordEmail(user, password);
    }

    private void sendPasswordEmail(final SystemUser user, final String password)
        throws NoSuchAlgorithmException, UnsupportedEncodingException, MessagingException {
        Session session = JavaMailUtilsFactory.getJavaMailUtilsInstance().getJavaMailSession();

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("support@funkyandroid.com"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
        message.setSubject("Conference Engineer Password");
        message.setText("Your password for Conference Engineer is : " + password);
        message.setHeader("X-Mailer", "ConferenceEngineerAutomailer");
        message.setSentDate(new Date());

        Transport.send(message);
    }

    private static final class InstanceHolder {
        private static final PasswordGenerator INSTANCE = new PasswordGenerator();
    }

    public static final PasswordGenerator getInstance() {
        return InstanceHolder.INSTANCE;
    }
}
