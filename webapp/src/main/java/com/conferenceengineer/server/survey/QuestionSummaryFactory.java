package com.conferenceengineer.server.survey;

import com.conferenceengineer.server.datamodel.SurveyQuestion;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by alsutton on 23/10/2014.
 */
public class QuestionSummaryFactory {

    public static QuestionSummaryCreator getSummariserFor(final SurveyQuestion question) {
        int type = question.getType();
        switch(type) {
            case SurveyQuestion.TYPE_FIVE_STAR_RATING:
                return RatingSummariser.getInstance();
            case SurveyQuestion.TYPE_FREE_TEXT :
                return FreeTextSummariser.getInstance();
            default:
                Logger.getAnonymousLogger().log(
                        Level.SEVERE,
                        "Unknown question type for "+question.getId()+" - "+question.getType()
                );
                return null;
        }

    }
}
