package com.conferenceengineer.server.utils.authentication;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.conferenceengineer.server.datamodel.UserAuthenticationInformation;
import com.conferenceengineer.server.datamodel.UserAuthenticationInformationDAO;

/**
 * Authentication information verifier for the internal user database
 */
public class InternalUserAuthenticationVerifier implements UserAuthenticationVerifier {
    @Override
    public boolean isUserAuthenticated(String username, String password,
                                       UserAuthenticationInformation authenticationInformation) {
        try {
            String information = authenticationInformation.getInformation();
            if(information == null || information.isEmpty()) {
                return false;
            }

            byte[] salt = new byte[4];
            for(int i = 0 ; i < 8 ; i+=2) {
                salt[i/2] = (byte) ((Character.digit(information.charAt(i), 16)<<4)
                                   + Character.digit(information.charAt(i+1), 16));
            }

            String test = UserAuthenticationInformationDAO.getInstance().createPasswordDigest(salt, password);
            return test.equals(information.substring(8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
