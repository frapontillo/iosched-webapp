package com.conferenceengineer.server.datamodel;

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

/**
 * Object which tracks the last modification and export times for objects.
 */
@Entity
@Table( name = "last_export_details" )
public class LastExport {

    public static final int TYPE_IOSCHED14 = 1;

    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="conference_id")
    private Conference conference;

    @Column(name="name")
    private String name;

    @Column(name="export_type")
    private int exportType;

    @Column(name="last_export")
    @Temporal(value= TemporalType.TIMESTAMP)
    private Calendar lastExport;

    @Column(name="serial_number")
    private int serialNumber;

    public LastExport() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExportType() {
        return exportType;
    }

    public void setExportType(int exportType) {
        this.exportType = exportType;
    }

    public Calendar getLastExport() {
        return lastExport;
    }

    public void setLastExport(Calendar lastExport) {
        this.lastExport = lastExport;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }
}
