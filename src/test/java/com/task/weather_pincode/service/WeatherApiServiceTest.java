package com.task.weather_pincode.service;


import com.task.weather_pincode.exception.ExternalApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RestClientTest(WeatherApiServiceImpl.class)
@TestPropertySource(properties = {
        "openweather.api.key=YOUR-API",
        "openweather.api.url=https://api.openweathermap.org/data/2.5/weather"
})
class WeatherApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherApiServiceImpl weatherApiService;

    @BeforeEach
    void setup() {
        weatherApiService = new WeatherApiServiceImpl();
        weatherApiService.openWeatherApiKey = "YOUR_API";
        weatherApiService.openWeatherApiUrl = "https://api.openweathermap.org/data/2.5/weather";
        weatherApiService.restTemplate = restTemplate;
    }

    @Test
    void shouldReturnWeatherDataSuccessfully() {
        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, Object> main = new HashMap<>();
        main.put("temp", 30.5);
        main.put("humidity", 70);
        mockResponse.put("main", main);

        Map<String, Object> wind = new HashMap<>();
        wind.put("speed", 5.2);
        mockResponse.put("wind", wind);

        Map<String, Object> weather = new HashMap<>();
        weather.put("description", "Clear sky");
        mockResponse.put("weather", List.of(weather));

        when(restTemplate.getForObject(
                anyString(),
                eq(Map.class)
        )).thenReturn(mockResponse);

        String result = weatherApiService.getWeatherData("18.5204", "73.8567");

        assertNotNull(result);
        assertTrue(result.contains("Temperature: 30.50°C"));
        assertTrue(result.contains("Humidity: 70%"));
        assertTrue(result.contains("Wind Speed: 5.20 m/s"));
        assertTrue(result.contains("Condition: Clear sky"));
    }

    @Test
    void shouldThrowExceptionWhenOpenWeatherApiReturnsNull() {
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenReturn(null);

        ExternalApiException exception = assertThrows(ExternalApiException.class, () ->
                weatherApiService.getWeatherData("18.5204", "73.8567")
        );

        assertTrue(exception.getMessage().contains("Failed to fetch weather data from OpenWeather"));
    }

    @Test
    void shouldThrowExceptionWhenOpenWeatherApiFails() {
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenThrow(new RuntimeException("API Error"));

        ExternalApiException exception = assertThrows(ExternalApiException.class, () ->
                weatherApiService.getWeatherData("18.5204", "73.8567")
        );

        assertEquals("Error calling OpenWeather API: API Error", exception.getMessage());
    }
}
