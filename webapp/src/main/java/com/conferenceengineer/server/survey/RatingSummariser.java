package com.conferenceengineer.server.survey;

import com.conferenceengineer.server.datamodel.SurveyAnswer;
import com.conferenceengineer.server.datamodel.SurveyQuestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingSummariser implements QuestionSummaryCreator {

    @Override
    public List<String> createSummary(SurveyQuestion question) {
        return createSummary(question, question.getSubmittedAnswers());
    }

    @Override
    public List<String> createSummary(SurveyQuestion question, List<SurveyAnswer> answers) {
        if(question.getType() != SurveyQuestion.TYPE_FIVE_STAR_RATING) {
            List <String> response = new ArrayList<>();
            response.add(UNABLE_TO_SUMMARISE);
            return response;
        }

        if(answers.isEmpty()) {
            List <String> response = new ArrayList<>();
            response.add(NO_RESPONSES);
            return response;
        }

        return summarise(answers);
    }

    protected List<String> summarise(List<SurveyAnswer> answers) {
        Map<Integer, CountHolder> responses = new HashMap<>();

        for(SurveyAnswer answer : answers) {
            int numericAnswer = Integer.parseInt(answer.getAnswer());

            CountHolder currentCount = responses.get(numericAnswer);
            if(currentCount == null) {
                responses.put(numericAnswer, new CountHolder());
            } else {
                currentCount.count++;
            }
        }

        return createSummary(responses);
    }

    private List<String> createSummary(Map<Integer, CountHolder> responses) {
        List<String> summary = new ArrayList<>();

        for(int i = 5 ; i > 0 ; i--) {
            addCountToSummary(summary, i, responses.get(i));
        }

        return summary;
    }

    private void addCountToSummary(List<String> summary, int value, CountHolder count) {
        StringBuilder buffer = new StringBuilder();
        addStars(buffer, value);
        if(count == null) {
            buffer.append(" : 0");
        } else {
            buffer.append(" : " + count.count);
        }
        summary.add(buffer.toString());
    }

    private void addStars(StringBuilder builder, int starCount) {
        int i = 0;
        for( ; i < starCount ; i++) {
            builder.append("<span class=\"glyphicon glyphicon-star\"></span>");
        }
        for( ; i < 5 ; i++) {
            builder.append("<span class=\"glyphicon glyphicon-star-empty\"></span>");
        }
    }

    private class CountHolder {
        int count = 1;
    }


    private static class InstanceHolder {
        private static final RatingSummariser INSTANCE = new RatingSummariser();
    }

    public static RatingSummariser getInstance() {
        return InstanceHolder.INSTANCE;
    }
}
