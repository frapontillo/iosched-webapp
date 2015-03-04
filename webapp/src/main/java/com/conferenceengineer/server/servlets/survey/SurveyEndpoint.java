package com.conferenceengineer.server.servlets.survey;

import com.conferenceengineer.server.datamodel.Conference;
import com.conferenceengineer.server.datamodel.Survey;
import com.conferenceengineer.server.datamodel.SurveyAnswer;
import com.conferenceengineer.server.datamodel.SurveyQuestion;
import com.conferenceengineer.server.datamodel.SurveyResponse;
import com.conferenceengineer.server.datamodel.Talk;
import com.conferenceengineer.server.datamodel.utils.EntityManagerFactoryWrapper;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SurveyEndpoint extends HttpServlet {
    private static final String PARAMETER_EVENT_CODE = "code";
    private static final String PARAMETER_API_KEY = "apikey";

    private static final String PARAMETER_SESSION_ID = "objectid";
    private static final String PARAMETER_SURVEY_ID = "surveyId";
    private static final String PARAMETER_REGISTRANT_ID = "registrantKey";

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        String apiKey = request.getHeader(PARAMETER_API_KEY);

        if(apiKey == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            log("Invalid survey results "+request.getRemoteAddr());
            return;
        }

        EntityManager em = EntityManagerFactoryWrapper.getInstance().getEntityManager();

        try {
            Conference conference = getConferenceFromHeader(request, em);
            Talk talk = getTalkFromRequest(request, em);

            if (talk.getSlot().getConferenceDay().getConference().getId() != conference.getId()) {
                throw new RequestFormatException("Mismatch between session "+talk.getId()+" and conference "+conference.getId());
            }

            Survey survey = getSurveyFromRequest(request, em);
            if(survey.getConference().getId() != conference.getId()) {
                throw new RequestFormatException("Mismatch between survey "+survey.getId()+" and conference "+conference.getId());
            }
            if (!apiKey.equals(survey.getApiKey())) {
                throw new RequestFormatException("Incorrect API key specified for "+survey.getId());
            }

            String user = request.getParameter(PARAMETER_REGISTRANT_ID);
            if (user != null && !user.isEmpty()) {
                deletePreviousResponse(em, talk, user);
            }

            Map<Integer, SurveyQuestion> questionMap = constructPositionToQuestionMap(survey);
            storeUsersAnswers(request, em, questionMap, user, talk);
        } catch (RequestFormatException e) {
            handleIncorrectlyFormattedRequest(request, response, e.getMessage());
        } catch (Exception e) {
            log("Error during survey submission", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            em.close();
        }
    }

    private Conference getConferenceFromHeader(final HttpServletRequest request, final EntityManager em)
            throws RequestFormatException{
        return (Conference) getObjectFromIDInHeader(request, PARAMETER_EVENT_CODE, em, Conference.class);
    }

    private Object getObjectFromIDInHeader(final HttpServletRequest request, final String parameterName,
                                           final EntityManager em, final Class<?> objectClass)
            throws RequestFormatException {
        String id = request.getHeader(parameterName);
        return getObjectFromID(em, objectClass, id);
    }

    private Talk getTalkFromRequest(final HttpServletRequest request, final EntityManager em)
            throws RequestFormatException {
        return (Talk) getObjectFromIDInRequest(request, PARAMETER_SESSION_ID, em, Talk.class);
    }

    private Survey getSurveyFromRequest(final HttpServletRequest request, final EntityManager em)
            throws RequestFormatException {
        return (Survey) getObjectFromIDInRequest(request, PARAMETER_SURVEY_ID, em, Survey.class);
    }

    private Object getObjectFromIDInRequest(final HttpServletRequest request, final String parameterName,
                                                         final EntityManager em, final Class<?> objectClass)
            throws RequestFormatException {
        String id = request.getParameter(parameterName);
        return getObjectFromID(em, objectClass, id);
    }

    private Object getObjectFromID(final EntityManager em, final Class<?> objectClass, String id)
            throws RequestFormatException {
        if (id == null || id.isEmpty()) {
            throw new RequestFormatException("id not specified in request for "+objectClass.getName());
        }

        Object object = em.find(objectClass, Integer.parseInt(id));
        if(object == null) {
            throw new RequestFormatException("Unable to find "+objectClass.getName()+" for "+id);
        }

        return object;
    }

    private void handleIncorrectlyFormattedRequest(HttpServletRequest request, HttpServletResponse response,
                                                   final String message)
            throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        log(message+" from "+request.getRemoteAddr());
    }

    private void deletePreviousResponse(final EntityManager em, final Talk talk, final String user) {
        Query query = em.createQuery("DELETE FROM SurveyAnswer WHERE talk = :talk AND response.attendee = :attendee");
        query.setParameter("talk", talk);
        query.setParameter("attendee", user);
        query.executeUpdate();
    }

    private Map<Integer,SurveyQuestion> constructPositionToQuestionMap(final Survey survey) {
        Map<Integer,SurveyQuestion> questionMap = new HashMap<>();
        for(SurveyQuestion thisQuestion: survey.getQuestions()) {
            questionMap.put(thisQuestion.getPosition(), thisQuestion);
        }
        return questionMap;
    }

    private void storeUsersAnswers(final HttpServletRequest request, final EntityManager em,
                                   final Map<Integer,SurveyQuestion> questionMap,
                                   final String user, final Talk talk) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            SurveyResponse response = new SurveyResponse();
            response.setAttendee(user);
            em.persist(response);

            int i = 1;
            String userResponse;
            while((userResponse = request.getParameter("q"+i)) != null) {
                SurveyQuestion question = questionMap.get(i++);
                if(question == null) {
                    continue;
                }

                SurveyAnswer newResult = new SurveyAnswer();
                newResult.setResponse(response);
                if(userResponse != null && userResponse.isEmpty()) {
                    userResponse = null;
                }
                newResult.setAnswer(userResponse);
                newResult.setQuestion(question);
                newResult.setTalk(talk);
                em.persist(newResult);
            }
            transaction.commit();
        } catch (RuntimeException e) {
            if(transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    private class RequestFormatException extends ServletException {
        RequestFormatException(final String message) {
            super(message);
        }
    }
}
