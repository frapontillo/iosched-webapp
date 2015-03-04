package com.conferenceengineer.server.datamodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table( name = "survey_response" )
public class SurveyResponse {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "attendee")
    private String attendee;

    @OneToMany(mappedBy = "response")
    private List<SurveyAnswer> responses;

    public SurveyResponse() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAttendee() {
        return attendee;
    }

    public void setAttendee(String attendee) {
        this.attendee = attendee;
    }

    public List<SurveyAnswer> getResponses() {
        return responses;
    }

    public void setResponses(List<SurveyAnswer> responses) {
        this.responses = responses;
    }
}
