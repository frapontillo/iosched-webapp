package com.conferenceengineer.server.datamodel;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Representation of the metadata about the conference held in the database.
 */
@Entity
@Table( name = "conference_metadata" )
public class ConferenceMetadata {

    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;

    @OneToOne
    @JoinColumn(name="conference_id")
    private Conference conference;

    @Column(name="last_update")
    @Temporal(value=TemporalType.TIMESTAMP)
    private Calendar lastUpdate;

    @Column(name="last_published")
    @Temporal(value=TemporalType.TIMESTAMP)
    private Calendar lastPublished;

    public ConferenceMetadata() {
        super();
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

    public Calendar getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Calendar lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Calendar getLastPublished() {
        return lastPublished;
    }

    public void setLastPublished(Calendar lastPublished) {
        this.lastPublished = lastPublished;
    }
}
