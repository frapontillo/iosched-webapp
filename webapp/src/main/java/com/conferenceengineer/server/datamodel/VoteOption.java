package com.conferenceengineer.server.datamodel;

import javax.persistence.*;

/**
 * Representation of a user in the system
 */
@Entity
@Table( name = "vote_option" )
public class VoteOption {

    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="vote_series_id")
    private VoteSeries series;

    @Column(name="name")
    private String name;

    @Column(name="relation_to")
    private int relationTo;

    @Column(name="relation_id")
    private int relationId;

    @Transient
    private Object referredObject;

    public VoteOption() {
        super();
    }

    public VoteOption(final String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VoteSeries getSeries() {
        return series;
    }

    public void setSeries(VoteSeries series) {
        this.series = series;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRelationTo() {
        return relationTo;
    }

    public void setRelationTo(int relationTo) {
        this.relationTo = relationTo;
    }

    public int getRelationId() {
        return relationId;
    }

    public void setRelationId(int relationId) {
        this.relationId = relationId;
    }

    public Object getReferredObject() {
        return referredObject;
    }

    public void setReferredObject(Object referredObject) {
        this.referredObject = referredObject;
    }
}
