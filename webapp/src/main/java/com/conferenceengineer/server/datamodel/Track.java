package com.conferenceengineer.server.datamodel;

import javax.persistence.*;
import java.util.List;

/**
 * Representation of a user in the system
 */
@Entity
@Table( name = "track" )
public class Track implements Comparable<Track>{

    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;

    @Column(name="track_name")
    private String name;

    @Column(name="colour")
    private String colour;

    @Column(name="description")
    private String description;

    @ManyToOne
    @JoinColumn(name="conference_id")
    private Conference conference;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "track")
    @OrderBy("name")
    private List<Talk> talkList;

    public Track() {
        super();
    }

    public Track(final Conference conference, final String name, final String description) {
        this.conference = conference;
        this.name = name;
        this.description = description;
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

    public String getColour() {
        if(colour != null) {
            colour = colour.trim();
        }
        return colour;
    }

    public void setColour(String colour) {
        if(colour != null) {
            colour = colour.trim();
        }
        this.colour = colour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Talk> getTalkList() {
        return talkList;
    }

    public void setTalkList(List<Talk> talkList) {
        this.talkList = talkList;
    }

    @Override
    public int compareTo(Track other) {
        if(name == null && other.name == null) {
            return 0;
        }
        if(name == null) {
            return -1;
        }
        if(other.name == null) {
            return 1;
        }
        return name.compareTo(other.name);
    }


    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Track)) {
            return false;
        }

        return compareTo((Track)other) == 0;
    }

    @Override
    public int hashCode() {
        return ((Integer)id).hashCode();
    }
}
