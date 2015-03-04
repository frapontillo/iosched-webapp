package com.conferenceengineer.server.datamodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;

/**
 * Object which tracks the last modification and export times for objects.
 */
@Entity
@Table( name = "last_modification_times" )
public class LastModification {

    public static final String  NAME_DAYS = "days",
                                NAME_HASHTAGS = "hashtags",
                                NAME_LOCATIONS = "locations",
                                NAME_SPEAKERS = "speakers",
                                NAME_TALKS = "talks",
                                NAME_TIME_SLOTS = "timeslots",
                                NAME_TRACKS = "tracks";

    public static final String[] TRACKED_ENTITY_NAMES =
            { NAME_DAYS, NAME_HASHTAGS, NAME_LOCATIONS, NAME_SPEAKERS,
                    NAME_TALKS, NAME_TIME_SLOTS, NAME_TRACKS };

    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="conference_id")
    private Conference conference;

    @Column(name="name")
    private String name;

    @Column(name="last_modification")
    @Temporal(value= TemporalType.TIMESTAMP)
    private Calendar lastModification;

    public LastModification() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getLastModification() {
        return lastModification;
    }

    public void setLastModification(Calendar lastModification) {
        this.lastModification = lastModification;
    }

}
