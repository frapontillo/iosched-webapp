package com.conferenceengineer.server.datamodel;

import javax.persistence.*;

/**
 * Representation of a user in the system
 */
@Entity
@Table( name = "talk_location" )
public class TalkLocation {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="location_name")
    private String name;

    @Column(name="address")
    private String address;

    /**
     * Optional Conference link if this location is for a specific conference
     */
    @ManyToOne
    @JoinColumn(name="conference_id")
    private Conference conference;

    public TalkLocation() {
        super();
    }

    public TalkLocation(final Conference conference, final String name, final String address) {
        this.conference = conference;
        this.name = name;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }
}
