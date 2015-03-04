package com.conferenceengineer.server.datamodel;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Representation of the metadata about the conference held in the database.
 */
@Entity
@Table( name = "survey" )
public class Survey {

    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;

    @ManyToOne
    private Conference conference;

    @Column(name="apikey")
    private String apiKey;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "survey")
    private List<SurveyQuestion> questions;

    public Survey() {
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

    public List<SurveyQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<SurveyQuestion> questions) {
        this.questions = questions;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
