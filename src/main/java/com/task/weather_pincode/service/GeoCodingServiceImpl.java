package com.task.weather_pincode.service;

import com.task.weather_pincode.exception.ExternalApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class GeoCodingServiceImpl implements GeoCodingService {

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    @Value("${openweather.api.key}")
    private String openWeatherApiKey;

    private final RestTemplate restTemplate;

    public GeoCodingServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<String, String> getLatLongFromPincode(String pincode) {
        try {
            // 1️⃣ Try Google Maps API
            return getLatLongFromGoogleMaps(pincode);
        } catch (Exception e) {
            // 2️⃣ Fallback to OpenWeather Geocoding API
            return getLatLongFromOpenWeather(pincode);
        }
    }

    // ✅ Google Maps Geocoding API
    private Map<String, String> getLatLongFromGoogleMaps(String pincode) {
        String url = String.format(
                "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s",
                pincode, googleMapsApiKey);

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.get("status").equals("OK")) {
            Map<String, Object> location = (Map<String, Object>)
                    ((Map<String, Object>) ((Map<String, Object>)
                            ((java.util.List<?>) response.get("results")).get(0))
                            .get("geometry")).get("location");

            Map<String, String> result = new HashMap<>();
            result.put("lat", location.get("lat").toString());
            result.put("lng", location.get("lng").toString());
            return result;
        }

        throw new ExternalApiException("Failed to fetch coordinates from Google Maps");
    }

    // ✅ OpenWeather Geocoding API
    private Map<String, String> getLatLongFromOpenWeather(String pincode) {
        String url = String.format(
                "http://api.openweathermap.org/geo/1.0/zip?zip=%s,IN&appid=%s",
                pincode, openWeatherApiKey);

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.get("lat") != null && response.get("lon") != null) {
            Map<String, String> result = new HashMap<>();
            result.put("lat", response.get("lat").toString());
            result.put("lng", response.get("lon").toString());
            return result;
        }

        throw new ExternalApiException("Failed to fetch coordinates from OpenWeather");
    }
}
