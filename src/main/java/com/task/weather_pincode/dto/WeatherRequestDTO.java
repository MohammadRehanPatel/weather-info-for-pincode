package com.task.weather_pincode.dto;

public class WeatherRequestDTO {

    private String pincode;
    private String forDate;

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getForDate() {
        return forDate;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }
}
