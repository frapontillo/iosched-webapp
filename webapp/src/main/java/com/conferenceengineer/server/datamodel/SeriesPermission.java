package com.conferenceengineer.server.datamodel;

import javax.persistence.*;

/**
 * Representation of a user in the system
 */
@Entity
@Table( name = "series_permission" )
public class SeriesPermission {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="series_id")
    private Series series;

    @ManyToOne
    @JoinColumn(name="users_id")
    private SystemUser systemUser;

    @Column(name="permission")
    private int permission;

    public SeriesPermission() {
        super();
    }

    public SeriesPermission(final Series series, final SystemUser systemUser, final int permission) {
        this.series = series;
        this.systemUser = systemUser;
        this.permission = permission;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series mSeries) {
        this.series = mSeries;
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
