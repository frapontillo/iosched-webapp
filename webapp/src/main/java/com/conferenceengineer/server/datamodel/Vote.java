package com.conferenceengineer.server.datamodel;

import javax.persistence.*;

/**
 * Representation of a voting series.
 */
@Entity
@Table( name = "vote" )
public class Vote {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="vote_series_id")
    private VoteSeries voteSeries;

    @ManyToOne
    @JoinColumn(name="users_id")
    private SystemUser systemUser;

    public Vote() {
        super();
    }

    public Vote(final VoteSeries voteSeries, final SystemUser systemUser) {
        this.voteSeries = voteSeries;
        this.systemUser = systemUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VoteSeries getVoteSeries() {
        return voteSeries;
    }

    public void setVoteSeries(VoteSeries voteSeries) {
        this.voteSeries = voteSeries;
    }

    public SystemUser getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(SystemUser systemUser) {
        this.systemUser = systemUser;
    }
}
