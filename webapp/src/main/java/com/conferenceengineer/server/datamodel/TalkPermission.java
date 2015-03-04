package com.conferenceengineer.server.datamodel;

import javax.persistence.*;

/**
 * Representation of a user in the system
 */
@Entity
@Table( name = "talk_permission" )
public class TalkPermission {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="talk_id")
    private Talk talk;

    @ManyToOne
    @JoinColumn(name="users_id")
    private SystemUser systemUser;

    @Column(name="permission")
    private int permission;

    public TalkPermission() {
        super();
    }

    public TalkPermission(final Talk talk, final SystemUser systemUser, final int permission) {
        this.talk = talk;
        this.systemUser = systemUser;
        this.permission = permission;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Talk getTalk() {
        return talk;
    }

    public void setTalk(Talk talk) {
        this.talk = talk;
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
