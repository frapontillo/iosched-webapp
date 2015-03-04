package com.conferenceengineer.server.datamodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Representation of the metadata about the conference held in the database.
 */
@Entity
@Table( name = "survey_answer" )
public class SurveyAnswer {

    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;

    @ManyToOne
    private Talk talk;

    @ManyToOne
    private SurveyQuestion question;

    @ManyToOne
    private SurveyResponse response;

    @Column(name="answer")
    private String answer;

    public SurveyAnswer() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Talk getTalk() {
        return talk;
    }

    public void setTalk(Talk talk) {
        this.talk = talk;
    }

    public SurveyQuestion getQuestion() {
        return question;
    }

    public void setQuestion(SurveyQuestion question) {
        this.question = question;
    }

    public SurveyResponse getResponse() {
        return response;
    }

    public void setResponse(SurveyResponse response) {
        this.response = response;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
