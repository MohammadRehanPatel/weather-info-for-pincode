package com.task.weather_pincode.service;

import com.task.weather_pincode.exception.ExternalApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RestClientTest(GeoCodingServiceImpl.class)
@Import(GeoCodingServiceImpl.class)
class GeoCodingServiceTest {

    private MockRestServiceServer server;

    private GeoCodingService geoCodingService;

    @BeforeEach
    void setup(MockRestServiceServer server, GeoCodingService geoCodingService) {
        this.server = server;
        this.geoCodingService = geoCodingService;
    }

    @Test
    void shouldReturnLatLongFromGoogleMaps() {
        server.expect(requestTo("https://maps.googleapis.com/maps/api/geocode/json?address=411014&key=YOUR_GOOGLE_MAPS_API_KEY"))
                .andRespond(withSuccess("{\"status\": \"OK\", \"results\": [{\"geometry\": {\"location\": {\"lat\": \"18.5204\", \"lng\": \"73.8567\"}}}]}", MediaType.APPLICATION_JSON));

        Map<String, String> result = geoCodingService.getLatLongFromPincode("452014");

        assertEquals("18.5204", result.get("lat"));
        assertEquals("73.8567", result.get("lng"));
    }

    @Test
    void shouldThrowExceptionForInvalidPincode() {
        server.expect(anything()).andRespond(withBadRequest());

        assertThrows(ExternalApiException.class, () ->
                geoCodingService.getLatLongFromPincode("000000"));
    }
}
