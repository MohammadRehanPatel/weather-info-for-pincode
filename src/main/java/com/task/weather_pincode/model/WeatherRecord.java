package com.task.weather_pincode.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class WeatherRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pincode;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String weatherData;

    @Column(nullable = false)
    private LocalDate recordDate;


    // Constructors
    public WeatherRecord() {}

    public WeatherRecord(String pincode, String latitude, String longitude, String weatherData, LocalDate recordDate) {
        this.pincode = pincode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.weatherData = weatherData;
        this.recordDate = recordDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(String weatherData) {
        this.weatherData = weatherData;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }
}
