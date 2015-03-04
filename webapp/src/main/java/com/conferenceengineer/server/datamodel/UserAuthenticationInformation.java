package com.conferenceengineer.server.datamodel;

import javax.persistence.*;

/**
 * Representation of a user in the system
 */
@Entity
@Table( name = "user_authentication_information" )
public class UserAuthenticationInformation {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="users_id")
    private SystemUser systemUser;

    @Column(name="authenticator_type")
    private int authenticatorType;

    @Column(name="information")
    private String information;


    public UserAuthenticationInformation() {
        super();
    }

    public UserAuthenticationInformation(final SystemUser systemUser, final int authenticatorType,
                                         final String information) {
        this.systemUser = systemUser;
        this.authenticatorType = authenticatorType;
        this.information = information;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SystemUser getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(SystemUser systemUser) {
        this.systemUser = systemUser;
    }

    public int getAuthenticatorType() {
        return authenticatorType;
    }

    public void setAuthenticatorType(int authenticatorType) {
        this.authenticatorType = authenticatorType;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
