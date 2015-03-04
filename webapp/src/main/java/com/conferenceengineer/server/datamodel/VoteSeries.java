package com.conferenceengineer.server.datamodel;

import javax.persistence.*;
import java.util.List;

/**
 * Representation of a voting series.
 */
@Entity
@Table( name = "vote_series" )
public class VoteSeries {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="title")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="relation_to")
    private int relationTo;

    @Column(name="relation_id")
    private int relationId;

    @OneToMany
    @JoinColumn(name = "vote_series_id")
    private List<VoteOption> options;

    public VoteSeries() {
        super();
    }

    public VoteSeries(final String name, final String description, final int relationTo,
                      final int relationId) {
        this.name = name;
        this.description = description;
        this.relationTo = relationTo;
        this.relationId = relationId;
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

    public void setName(String title) {
        this.name = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<VoteOption> getOptions() {
        return options;
    }

    public void setOptions(List<VoteOption> options) {
        this.options = options;
    }
}
