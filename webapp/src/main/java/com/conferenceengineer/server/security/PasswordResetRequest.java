package com.conferenceengineer.server.security;

import com.conferenceengineer.server.datamodel.SystemUser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;

@Entity
@Table(name = "password_reset_requests")
public class PasswordResetRequest {

    public static final int STATE_NEW = 0,
                            STATE_EMAIL_SENT = 1,
                            STATE_PERFORMED = 2;


    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private SystemUser user;

    @Column(name = "request_timestamp")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Calendar timestamp;

    private String secret;

    private Integer state;

    public PasswordResetRequest() {
        super();
    }

    public PasswordResetRequest(final SystemUser user, final String secret) {
        this.user = user;
        this.secret = secret;
        timestamp = Calendar.getInstance();
        state = STATE_NEW;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SystemUser getUser() {
        return user;
    }

    public void setUser(SystemUser user) {
        this.user = user;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
