package com.conferenceengineer.server.survey;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.Survey;
import com.conferenceengineer.server.datamodel.SurveyQuestion;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;

/**
 * Class to handle the initialisation of the IOSched 14 survey for an application
 */
public class IOSched14SurveyInitialiser {

    private static final String[] RATINGS = {
        "Rate this session overall",
        "How relevant was this session to your projects?",
        "The content was (Too basic -> Too Advanced)",
        "Speaker quality"
    };

    public static void addIOSched14Survey(final EntityManager em, final Conference conference) {
        Survey survey = conference.getSurvey();

        int i = 0;
        for(; i < RATINGS.length ; i++) {
            addQuestion(em, survey, i+1, RATINGS[i], SurveyQuestion.TYPE_FIVE_STAR_RATING);
        }

        addQuestion(em, survey, i+1, "Comments", SurveyQuestion.TYPE_FREE_TEXT);
    }

    private static void addQuestion(final EntityManager em, final Survey survey, final int position,
                             final String question, final int type) {
        SurveyQuestion surveyQuestion = new SurveyQuestion();
        surveyQuestion.setPosition(position);
        surveyQuestion.setQuestion(question);
        surveyQuestion.setSurvey(survey);
        surveyQuestion.setType(type);
        em.persist(surveyQuestion);
    }
}
