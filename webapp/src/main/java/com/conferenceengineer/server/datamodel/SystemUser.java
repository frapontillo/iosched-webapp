package com.conferenceengineer.server.datamodel;

import javax.persistence.*;
import java.util.List;

/**
 * Representation of a user in the system
 */
@Entity
@Table( name = "system_users" )
public class SystemUser {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="flags")
    private int flags;

    @Column(name="human_name")
    private String humanName;

    @Column(name="email")
    private String email;

    @Column(name="image")
    private String image;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "systemUser")
    private List<ConferencePermission> permissions;

    public SystemUser() {
        super();
    }

    public SystemUser(final String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHumanName() {
        return humanName;
    }

    public void setHumanName(String humanName) {
        this.humanName = humanName;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<ConferencePermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<ConferencePermission> permissions) {
        this.permissions = permissions;
    }

    public String toString() {
        if(humanName != null && !humanName.isEmpty()) {
            return humanName;
        }

        return email;
    }
}
