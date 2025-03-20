package com.task.weather_pincode.dto;

public class WeatherResponseDTO {

    private String pincode;
    private String date;
    private String weatherData;

    public WeatherResponseDTO(String pincode, String date, String weatherData) {
        this.pincode = pincode;
        this.date = date;
        this.weatherData = weatherData;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(String weatherData) {
        this.weatherData = weatherData;
    }
}
