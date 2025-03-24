package com.task.weather_pincode.service;


import com.task.weather_pincode.dto.WeatherResponseDTO;
import com.task.weather_pincode.exception.ExternalApiException;
import com.task.weather_pincode.model.WeatherRecord;
import com.task.weather_pincode.repository.WeatherRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private WeatherRecordRepository repository;

    @Mock
    private GeoCodingService geoCodingService;

    @Mock
    private WeatherApiService weatherApiService;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @BeforeEach
    void setup() {
        weatherService = new WeatherServiceImpl();
        weatherService.repository = repository;
        weatherService.geoCodingService = geoCodingService;
        weatherService.weatherApiService = weatherApiService;
    }

    @Test
    void shouldReturnCachedWeatherData() {
        LocalDate today = LocalDate.now();
        WeatherRecord record = new WeatherRecord(
                "411014",
                "18.5204",
                "73.8567",
                "{\"temp\":30, \"humidity\":50}",
                today
        );
        when(repository.findByPincodeAndRecordDate("411014", today))
                .thenReturn(Optional.of(record));

        WeatherResponseDTO response = weatherService.getWeatherForPincode("411014", today);

        assertNotNull(response);
        assertEquals("411014", response.getPincode());
        assertEquals(today.toString(), response.getDate());
        assertTrue(response.getWeatherData().contains("\"temp\":30"));
        verify(repository, times(1)).findByPincodeAndRecordDate("411014", today);
        verifyNoInteractions(geoCodingService);
        verifyNoInteractions(weatherApiService);
    }

    @Test
    void shouldFetchFromApiAndSave() {
        LocalDate today = LocalDate.now();
        when(repository.findByPincodeAndRecordDate("411014", today))
                .thenReturn(Optional.empty());

        when(geoCodingService.getLatLongFromPincode("411014"))
                .thenReturn(Map.of("lat", "18.5204", "lng", "73.8567"));

        String weatherData = "{\"temp\":30, \"humidity\":50}";
        when(weatherApiService.getWeatherData("18.5204", "73.8567"))
                .thenReturn(weatherData);

        WeatherResponseDTO response = weatherService.getWeatherForPincode("411014", today);

        assertNotNull(response);
        assertEquals("411014", response.getPincode());
        assertEquals(today.toString(), response.getDate());
        assertEquals(weatherData, response.getWeatherData());

        verify(repository, times(1)).save(any(WeatherRecord.class));
    }

    @Test
    void shouldThrowExceptionWhenGeoCodingFails() {
        LocalDate today = LocalDate.now();
        when(repository.findByPincodeAndRecordDate("411014", today))
                .thenReturn(Optional.empty());

        when(geoCodingService.getLatLongFromPincode("411014"))
                .thenThrow(new ExternalApiException("Failed to get lat/long"));

        ExternalApiException exception = assertThrows(ExternalApiException.class, () ->
                weatherService.getWeatherForPincode("411014", today)
        );

        assertEquals("Failed to get lat/long", exception.getMessage());

        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenWeatherApiFails() {
        LocalDate today = LocalDate.now();
        when(repository.findByPincodeAndRecordDate("411014", today))
                .thenReturn(Optional.empty());

        when(geoCodingService.getLatLongFromPincode("411014"))
                .thenReturn(Map.of("lat", "18.5204", "lng", "73.8567"));

        when(weatherApiService.getWeatherData("18.5204", "73.8567"))
                .thenThrow(new ExternalApiException("OpenWeather API error"));

        ExternalApiException exception = assertThrows(ExternalApiException.class, () ->
                weatherService.getWeatherForPincode("411014", today)
        );

        assertEquals("OpenWeather API error", exception.getMessage());

        verify(repository, never()).save(any());
    }

    @Test
    void shouldReturnPaginatedWeatherRecords() {
        LocalDate today = LocalDate.now();
        WeatherRecord record1 = new WeatherRecord(
                "411014",
                "18.5204",
                "73.8567",
                "{\"temp\":30, \"humidity\":50}",
                today
        );
        WeatherRecord record2 = new WeatherRecord(
                "411015",
                "19.1234",
                "74.5678",
                "{\"temp\":32, \"humidity\":45}",
                today.minusDays(1)
        );

        Page<WeatherRecord> mockPage = new PageImpl<>(List.of(record1, record2));
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable)).thenReturn(mockPage);

        Page<WeatherResponseDTO> result = weatherService.findAllWeatherRecord(0, 10);

        assertEquals(2, result.getContent().size());
        assertEquals("411014", result.getContent().get(0).getPincode());
        assertEquals("411015", result.getContent().get(1).getPincode());

        verify(repository, times(1)).findAll(pageable);
    }
}
