package com.task.weather_pincode.service;

import com.task.weather_pincode.exception.ExternalApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeoCodingServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GeoCodingServiceImpl geoCodingService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(geoCodingService, "googleMapsApiKey", "test-google-key");
        ReflectionTestUtils.setField(geoCodingService, "openWeatherApiKey", "test-openweather-key");
    }

    @Test
    void shouldReturnCoordinatesFromGoogleMaps() {
        String pincode = "411014";


        Map<String, Object> location = new HashMap<>();
        location.put("lat", 18.5685);
        location.put("lng", 73.9158);

        Map<String, Object> geometry = new HashMap<>();
        geometry.put("location", location);

        Map<String, Object> result = new HashMap<>();
        result.put("geometry", geometry);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("results", new Object[]{result});

        when(restTemplate.getForObject(contains("maps.googleapis.com"), eq(Map.class)))
                .thenReturn(response);

        Map<String, String> coordinates = geoCodingService.getLatLongFromPincode(pincode);

        assertEquals("18.5204", coordinates.get("lat"));
        assertEquals("73.8567", coordinates.get("lng"));
    }

    @Test
    void shouldFallBackToOpenWeatherWhenGoogleMapsFails() {
        String pincode = "411014";

        when(restTemplate.getForObject(contains("https://maps.googleapis.com"), eq(Map.class)))
                .thenThrow(new ExternalApiException("Google Maps API failure"));

        Map<String, Object> openWeatherResponse = new HashMap<>();
        openWeatherResponse.put("lat", 18.5685);
        openWeatherResponse.put("lon", 73.9158);

        when(restTemplate.getForObject(contains("http://api.openweathermap.org"), eq(Map.class)))
                .thenReturn(openWeatherResponse);

        Map<String, String> coordinates = geoCodingService.getLatLongFromPincode(pincode);


        assertNotNull(coordinates);
        assertEquals("18.5685", coordinates.get("lat"));
        assertEquals("73.9158", coordinates.get("lng"));

        verify(restTemplate, times(1)).getForObject(
                contains("https://maps.googleapis.com/maps/api/geocode/json"), eq(Map.class)
        );
        verify(restTemplate, times(1)).getForObject(
                contains("http://api.openweathermap.org/geo/1.0/zip"), eq(Map.class)
        );
    }

    @Test
    void shouldThrowExceptionWhenBothApisFail() {
        String pincode = "411014";

        when(restTemplate.getForObject(contains("https://maps.googleapis.com"), eq(Map.class)))
                .thenThrow(new ExternalApiException("Failed to fetch coordinates from Google Maps"));

        when(restTemplate.getForObject(contains("http://api.openweathermap.org"), eq(Map.class)))
                .thenThrow(new ExternalApiException("Failed to fetch coordinates from OpenWeather"));

        ExternalApiException exception = assertThrows(
                ExternalApiException.class,
                () -> geoCodingService.getLatLongFromPincode(pincode)
        );

        assertEquals("Failed to fetch coordinates from OpenWeather", exception.getMessage());

        verify(restTemplate, times(1)).getForObject(
                contains("https://maps.googleapis.com/maps/api/geocode/json"), eq(Map.class)
        );
        verify(restTemplate, times(1)).getForObject(
                contains("http://api.openweathermap.org/geo/1.0/zip"), eq(Map.class)
        );
    }
}
