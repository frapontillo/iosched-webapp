package com.conferenceengineer.server.datamodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table( name = "publication_endpoint" )
public class PublicationEndpoint {

    public static final int TYPE_IOSCHED13 = 1,
                            TYPE_IOSCHED14 = 2;

    private static final Map<Integer, String> ENDPOINT_NAMES = new HashMap<>();
    static {
        ENDPOINT_NAMES.put(TYPE_IOSCHED13, "iosched13 derived app");
        ENDPOINT_NAMES.put(TYPE_IOSCHED14, "iosched14 derived app");
    }

    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="conference_id")
    private Conference conference;

    @Column(name="type")
    private Integer type;

    @Column(name="url")
    private String url;

    @Column(name="apiKey")
    private String apiKey;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Transient
    public String getName() {
        return ENDPOINT_NAMES.get(getType());
    }
}
