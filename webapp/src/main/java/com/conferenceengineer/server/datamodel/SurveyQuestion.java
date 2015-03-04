package com.conferenceengineer.server.datamodel;

import com.conferenceengineer.server.survey.FreeTextSummariser;
import com.conferenceengineer.server.survey.QuestionSummaryCreator;
import com.conferenceengineer.server.survey.QuestionSummaryFactory;
import com.conferenceengineer.server.survey.RatingSummariser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Entity
@Table( name = "survey_question" )
public class SurveyQuestion {

    public static final int TYPE_FIVE_STAR_RATING = 1,
                            TYPE_FREE_TEXT = 2;

    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;

    @ManyToOne
    private Survey survey;

    @Column(name="question")
    private String question;

    @Column(name="type")
    private Integer type;

    @Column(name="position")
    private Integer position;

    @OneToMany(mappedBy = "question")
    private List<SurveyAnswer> submittedAnswers;

    public SurveyQuestion() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public List<SurveyAnswer> getSubmittedAnswers() {
        return submittedAnswers;
    }

    public void setSubmittedAnswers(List<SurveyAnswer> submittedAnswers) {
        this.submittedAnswers = submittedAnswers;
    }

    /**
     * Get a list of ordered strings which should be used to show a summary of the results.
     *
     * @return A List of Strings which should be shown in order
     */
    @Transient
    public List<String> getSummary() {
        QuestionSummaryCreator summariser = QuestionSummaryFactory.getSummariserFor(this);
        if(summariser == null) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Unknown question type for "+getId()+" - "+getType());
            return null;
        }
        return summariser.createSummary(this);
    }
}
