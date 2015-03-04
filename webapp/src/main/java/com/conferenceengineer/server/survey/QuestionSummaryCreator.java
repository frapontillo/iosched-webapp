package com.conferenceengineer.server.survey;

import com.conferenceengineer.server.datamodel.SurveyAnswer;
import com.conferenceengineer.server.datamodel.SurveyQuestion;

import java.util.List;

/**
 * Interface for classes which should create a summary for a question
 */
public interface QuestionSummaryCreator {

    static final String UNABLE_TO_SUMMARISE = "No summary available";

    static final String NO_RESPONSES = "No responses have been submitted to this question";

    public List<String> createSummary(SurveyQuestion question);

    public List<String> createSummary(SurveyQuestion question, List<SurveyAnswer> answers);
}
