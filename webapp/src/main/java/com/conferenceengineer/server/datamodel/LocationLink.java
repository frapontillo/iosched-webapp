package com.conferenceengineer.server.datamodel;

import javax.persistence.*;

/**
 * Representation of a link which relates to a particular location.
 */
@Entity
@Table( name = "location_links" )
public class LocationLink {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="location_id")
    private Location location;

    @Column(name="link_name")
    private String name;

    @Column(name="link")
    private String link;

    public LocationLink() {
        super();
    }

    public LocationLink(final Location location, final String name, final String link) {
        this.location = location;
        this.name = name;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
