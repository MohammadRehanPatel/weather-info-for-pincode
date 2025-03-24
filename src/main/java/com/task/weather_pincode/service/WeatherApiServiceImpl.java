package com.task.weather_pincode.service;

import com.task.weather_pincode.exception.ExternalApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class WeatherApiServiceImpl implements  WeatherApiService{
    @Value("${openweather.api.key}")
    String openWeatherApiKey;

    @Value("${openweather.api.url}")
    String openWeatherApiUrl;

    RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getWeatherData(String latitude, String longitude) {
        try {
            String url = String.format(
                    "%s?lat=%s&lon=%s&appid=%s&units=metric",
                    openWeatherApiUrl, latitude, longitude, openWeatherApiKey
            );

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null) {
                return convertToReadableFormat(response);
            } else {
                throw new ExternalApiException("Failed to fetch weather data from OpenWeather");
            }
        } catch (Exception e) {
            throw new ExternalApiException("Error calling OpenWeather API: " + e.getMessage());
        }
    }

    private String convertToReadableFormat(Map<String, Object> response) {
        Map<String, Object> main = (Map<String, Object>) response.get("main");
        Map<String, Object> wind = (Map<String, Object>) response.get("wind");
        Map<String, Object> weather = (Map<String, Object>) ((java.util.List<?>) response.get("weather")).get(0);

        double temperature = ((Number) main.get("temp")).doubleValue();
        double humidity = ((Number) main.get("humidity")).doubleValue();
        double windSpeed = ((Number) wind.get("speed")).doubleValue();
        String condition = (String) weather.get("description");

        return String.format(
                "Temperature: %.2f°C, Humidity: %.0f%%, Wind Speed: %.2f m/s, Condition: %s",
                temperature,
                humidity,
                windSpeed,
                condition
        );
    }
}
