package com.conferenceengineer.server.datamodel;

import javax.persistence.*;

/**
 * Representation of a user in the system
 */
@Entity
@Table( name = "conference_permission" )
public class ConferencePermission {

    /**
     * The permissions for a user.
     */
    public static final int PERMISSION_ADMINISTER_COLLABORATORS = 0x01;


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="conference_id")
    private Conference conference;

    @ManyToOne
    @JoinColumn(name="users_id")
    private SystemUser systemUser;

    @Column(name="permission")
    private int permission;

    public ConferencePermission() {
        super();
    }

    public ConferencePermission(final Conference conference, final SystemUser systemUser, final int permission) {
        this.conference = conference;
        this.systemUser = systemUser;
        this.permission = permission;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public SystemUser getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(SystemUser systemUser) {
        this.systemUser = systemUser;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }
}
