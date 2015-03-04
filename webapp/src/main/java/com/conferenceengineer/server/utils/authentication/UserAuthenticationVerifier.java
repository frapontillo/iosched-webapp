package com.conferenceengineer.server.utils.authentication;


import com.conferenceengineer.server.datamodel.UserAuthenticationInformation;

/**
 * Interface for classes which verify a users login details are valid given the credentiations and the authentication
 * information.
 */
public interface UserAuthenticationVerifier {

    /**
     * Check to see if a user can be authenticated with the given information
     *
     * @param username The username for the user.
     * @param password The password for the user.
     * @param authenticationInformation The authentication information to check.
     * @retun true if the user can be authenticated using the information, false if not.
     */

    public boolean isUserAuthenticated(final String username, final String password,
                                       final UserAuthenticationInformation authenticationInformation);
}
