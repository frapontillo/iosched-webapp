package com.conferenceengineer.server.security;

import com.conferenceengineer.server.datamodel.SystemUser;

import javax.persistence.EntityManager;

public class PasswordResetRequestManager {

    public static PasswordResetRequest create(final EntityManager entityManager,
                                              final SystemUser user, final String secret) {
        PasswordResetRequest request = new PasswordResetRequest(user, secret);
        entityManager.persist(request);
        return request;
    }

    public static PasswordResetRequest getPasswordResetRequest(final EntityManager entityManager, int id) {
        return entityManager.find(PasswordResetRequest.class, id);
    }
}
