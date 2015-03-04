package com.conferenceengineer.server.datamodel;

import javax.persistence.*;
import java.util.Set;

/**
 * Representation of a user in the system
 */
@Entity
@Table( name = "presenter" )
public class Presenter {

    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="image_url")
    private String imageURL;

    @Column(name="social_link")
    private String socialLink;

    @Column(name="short_biography")
    private String shortBiography;

    @Column(name="long_biography")
    private String longBiography;

    @ManyToOne
    @JoinColumn(name="conference_id")
    private Conference conference;

    @ManyToOne
    @JoinColumn(name="user_id")
    private SystemUser user;

    @ManyToMany(mappedBy="presenters")
    private Set<Talk> talks;

    public Presenter() {
        super();
    }

    public Presenter(final Conference conference, final String name, final String imageURL, final String socialURL,
                     final String shortBiography, final String longBiography) {
        this.conference = conference;
        this.name = name;
        this.imageURL = imageURL;
        this.socialLink = socialURL;
        this.shortBiography = shortBiography;
        this.longBiography = longBiography;
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSocialLink() {
        return socialLink;
    }

    public void setSocialLink(String socialLink) {
        this.socialLink = socialLink;
    }

    public String getShortBiography() {
        return shortBiography;
    }

    public void setShortBiography(String shortBiography) {
        this.shortBiography = shortBiography;
    }

    public String getLongBiography() {
        return longBiography;
    }

    public void setLongBiography(String longBiography) {
        this.longBiography = longBiography;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public SystemUser getUser() {
        return user;
    }

    public void setUser(SystemUser user) {
        this.user = user;
    }

    public Set<Talk> getTalks() {
        return talks;
    }

    public void setTalks(Set<Talk> talks) {
        this.talks = talks;
    }

    public String toString() {
        return getName();
    }
}
