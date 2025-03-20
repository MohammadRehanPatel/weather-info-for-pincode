package com.task.weather_pincode.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenWeatherConfig {

    @Value("${openweather.api.key}")
    private String openWeatherApiKey;

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    @Bean
    public String getOpenWeatherApiKey() {
        return openWeatherApiKey;
    }

    @Bean
    public String getGoogleMapsApiKey() {
        return googleMapsApiKey;
    }
}
