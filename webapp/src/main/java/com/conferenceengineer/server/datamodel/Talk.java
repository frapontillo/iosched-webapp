package com.conferenceengineer.server.datamodel;

import com.conferenceengineer.server.survey.QuestionSummaryCreator;
import com.conferenceengineer.server.survey.QuestionSummaryFactory;

import javax.persistence.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Representation of a user in the system
 */
@Entity
@Table( name = "talk" )
public class Talk {

    /**
     * The types of talks.
     */

    public static final int TYPE_ACCEPTED = 0x00,
                            TYPE_PROPOSED = 0x01,
                            TYPE_KEYNOTE = 0x02;

    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="conference_id")
    private Conference conference;

    @ManyToOne
    @JoinColumn(name="track_id")
    private Track track;

    @ManyToOne
    @JoinColumn(name="talk_slot_id")
    private TalkSlot slot;

    @ManyToOne
    @JoinColumn(name="talk_location_id")
    private TalkLocation location;

    @Column(name="type")
    private Integer type;

    @Column(name="title")
    private String name;

    @Column(name="abstract")
    private String shortDescription;

    @Column(name="long_description")
    private String longDescription;

    @Column(name="information_url")
    private String informationURL;

    @ManyToMany
    @JoinTable(name="talk_presenter")
    @OrderBy("name")
    private Set<Presenter> presenters;

    @OneToMany(mappedBy = "talk")
    private List<SurveyAnswer> answers;

    public Talk() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public TalkSlot getSlot() {
        return slot;
    }

    public void setSlot(TalkSlot slot) {
        this.slot = slot;
    }

    public TalkLocation getLocation() {
        return location;
    }

    public void setLocation(TalkLocation location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getInformationURL() {
        return informationURL;
    }

    public void setInformationURL(String informationURL) {
        this.informationURL = informationURL;
    }

    public Set<Presenter> getPresenters() {
        return presenters;
    }

    public void setPresenters(Set<Presenter> presenters) {
        this.presenters = presenters;
    }

    public List<SurveyAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<SurveyAnswer> answers) {
        this.answers = answers;
    }

    @Transient
    public List<SurveyResponses> getSurveyResponses() {
        Map<SurveyQuestion, List<SurveyAnswer>> answers = collectAnswersForQuestions();

        List<SurveyResponses> responses = new ArrayList<SurveyResponses>();
        for(Map.Entry<SurveyQuestion, List<SurveyAnswer>> thisEntry : answers.entrySet()) {
            SurveyResponses response = new SurveyResponses(thisEntry.getKey(), thisEntry.getValue());
            responses.add(response);
        }
        Collections.sort(responses, SURVEY_RESPONSES_COMPARATOR);

        return responses;
    }


    private Map<SurveyQuestion, List<SurveyAnswer>> collectAnswersForQuestions() {
        Map<SurveyQuestion, List<SurveyAnswer>> answers = new HashMap<>();

        for(SurveyAnswer answer : getAnswers()) {
            SurveyQuestion question = answer.getQuestion();
            List<SurveyAnswer> answersForQuestion = answers.get(question);
            if(answersForQuestion == null) {
                answersForQuestion = new ArrayList<>();
                answers.put(question, answersForQuestion);
            }
            answersForQuestion.add(answer);
        }

        return answers;
    }

    public class SurveyResponses {
        private SurveyQuestion mQuestion;
        private List<SurveyAnswer> mAnswers;

        SurveyResponses(SurveyQuestion question, List<SurveyAnswer> answers) {
            mQuestion = question;
            mAnswers = answers;
        }

        public SurveyQuestion getQuestion() {
            return mQuestion;
        }

        public List<SurveyAnswer> getAnswers() {
            return mAnswers;
        }

        public List<String> getSummaries() {
            QuestionSummaryCreator summariser = QuestionSummaryFactory.getSummariserFor(mQuestion);
            if(summariser == null) {
                return null;
            }
            return summariser.createSummary(mQuestion, mAnswers);
        }

    }

    private static final Comparator<SurveyResponses> SURVEY_RESPONSES_COMPARATOR =
            new Comparator<SurveyResponses>() {
                @Override
                public int compare(SurveyResponses o1, SurveyResponses o2) {
                    return o1.getQuestion().getPosition() - o2.getQuestion().getPosition();
                }
            };
}
