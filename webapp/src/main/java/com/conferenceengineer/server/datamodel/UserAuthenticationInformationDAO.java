package com.conferenceengineer.server.datamodel;

import org.apache.commons.codec.binary.Base64;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

/**
 * Data Accessor Object for UserAuthenticationInformation objects
 */
public class UserAuthenticationInformationDAO {

    /**
     * The supported authenticator types
     */

    public static final int AUTHENTICATOR_INTERNAL = 0;

    /**
     * Character array used in byte to hex conversion
     */

    private static final char[] HEX_ARRAY = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

    /**
     * The secure random number generator for salts
     */

    private static final SecureRandom sSecureRandom = new SecureRandom();

    /**
     * Private constructor to enforce instantiation
     */

    private UserAuthenticationInformationDAO() {
        super();
    }

    /**
     * Store the information about the authentication credentials for an external authenticator.
     *
     * @param entityManager The EntityManager currently in use.
     * @param systemUser The user this configuration information is relevant to.
     * @param authenticatorType The type of authenticator this information belongs to.
     * @param externalIdentifier The identifier passed by the external authenticator to represent the user.
     */
    public void store(final EntityManager entityManager, final SystemUser systemUser, final int authenticatorType,
                      final String externalIdentifier) {
        entityManager.persist(new UserAuthenticationInformation(systemUser, authenticatorType, externalIdentifier));
    }

    /**
     * Create an internal authentication entry for a user.
     *
     * @param entityManager The EntityManager currently in use.
     * @param systemUser The user this configuration information is relevant to.
     * @param password The password to store.
     */

    public void createInternalAuthenticationEntry(final EntityManager entityManager, final SystemUser systemUser,
                                                  final String password)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if(password == null || password.isEmpty()) {
            throw new RuntimeException("The password can not be empty");
        }

        store(entityManager, systemUser, AUTHENTICATOR_INTERNAL, createInternalAuthenticatorString(password));
    }

    /**
     * Create the string to be used for internal authenticators
     */

    public String createInternalAuthenticatorString(final String password)
        throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] salt = new byte[4];
        synchronized (sSecureRandom) {
            sSecureRandom.nextBytes(salt);
        }

        String passwordHash = createPasswordDigest(salt, password);
        StringBuilder information = new StringBuilder(8+passwordHash.length());
        for(int i = 0 ; i < salt.length ; i++) {
            int thisByte = salt[i] & 0xff;
            information.append(HEX_ARRAY[thisByte>>>4]);
            information.append(HEX_ARRAY[thisByte&0x0f]);
        }
        information.append(passwordHash);
        return information.toString();
    }

    /**
     * Create the hash for a given password.
     *
     * @param salt The salt to use.
     * @param password The password to use.
     */

    public String createPasswordDigest(final byte[] salt, final String password)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(salt);
        byte[] digestedPassword = digest.digest(password.getBytes("UTF-8"));
        for(int i = 0 ; i < 5 ; i++) {
            digest.reset();
            digestedPassword = digest.digest(digestedPassword);
        }

        return Base64.encodeBase64String(digestedPassword);
    }

    /**
     * Get all the authenticators for a user.
     *
     * @param entityManager The entity manager currently in use.
     * @param systemUser The user to get the authenticators for.
     * @return The list of authenticators.
     */

    @SuppressWarnings("unchecked")
    public List<UserAuthenticationInformation> getAuthenticators(final EntityManager entityManager,
                                                         final SystemUser systemUser) {
        Query q = entityManager.createQuery("SELECT x FROM UserAuthenticationInformation x WHERE x.systemUser = :user");
        q.setParameter("user", systemUser);
        return (List<UserAuthenticationInformation>)q.getResultList();
    }

    //------------------------ Singleton pattern to fetch this DAO ----------------------------------

    private static final class InstanceHolder {
        private static final UserAuthenticationInformationDAO INSTANCE = new UserAuthenticationInformationDAO();
    }

    public static UserAuthenticationInformationDAO getInstance() {
        return InstanceHolder.INSTANCE;
    }
}
