package com.conferenceengineer.server.datamodel;

import javax.persistence.*;

/**
 * Representation of a user in the system
 */
@Entity
@Table( name = "conference_configuration" )
public class ConferenceConfiguration {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="conference_id")
    private Conference conference;

    @Column(name="property_name")
    private String propertyName;

    @Column(name="property_value")
    private String propertyValue;

    public ConferenceConfiguration() {
        super();
    }

    public ConferenceConfiguration(final Conference conference, final String propertyName, final String propertyValue) {
        this.conference = conference;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
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

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }
}
