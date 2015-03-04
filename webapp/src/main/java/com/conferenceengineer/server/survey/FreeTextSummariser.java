package com.conferenceengineer.server.survey;

import com.conferenceengineer.server.datamodel.SurveyAnswer;
import com.conferenceengineer.server.datamodel.SurveyQuestion;

import java.util.ArrayList;
import java.util.List;

public class FreeTextSummariser implements QuestionSummaryCreator {

    @Override
    public List<String> createSummary(SurveyQuestion question) {
        return createSummary(question, question.getSubmittedAnswers());
    }

    @Override
    public List<String> createSummary(SurveyQuestion question, List<SurveyAnswer> answers) {
        List <String> response = new ArrayList<>();

        response.add("<i>");
        int count = 0;
        for(SurveyAnswer answer : answers) {
            String text = answer.getAnswer();
            if(text == null || text.isEmpty()) {
                continue;
            }

            response.add(text+"<br />");
            count++;
        }

        if(count == 0) {
            response.add("No responses received");
        }
        response.add("</i>");

        return response;
    }
    private static class InstanceHolder {
        private static final FreeTextSummariser INSTANCE = new FreeTextSummariser();
    }

    public static FreeTextSummariser getInstance() {
        return InstanceHolder.INSTANCE;
    }
}
