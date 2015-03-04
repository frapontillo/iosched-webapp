package com.conferenceengineer.server.datamodel;

import javax.persistence.*;

/**
 * Representation of a user in the system
 */
@Entity
@Table( name = "series_configuration" )
public class SeriesConfiguration {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="series_id")
    private Series series;

    @Column(name="property_name")
    private String propertyName;

    @Column(name="property_value")
    private String propertyValue;

    public SeriesConfiguration() {
        super();
    }

    public SeriesConfiguration(final Series series, final String propertyName, final String propertyValue) {
        this.series = series;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series mSeries) {
        this.series = mSeries;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }
}
