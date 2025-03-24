package com.task.weather_pincode.controller;


import com.task.weather_pincode.dto.WeatherRequestDTO;
import com.task.weather_pincode.dto.WeatherResponseDTO;
import com.task.weather_pincode.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "Weather", description = "Endpoints for managing weather data by pincode")
@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @Operation(summary = "Get weather data", description = "Fetch weather data using pincode and date.")
    @PostMapping
    public ResponseEntity<WeatherResponseDTO> getWeather(@RequestBody WeatherRequestDTO request) {
        LocalDate date = LocalDate.parse(request.getForDate());
        WeatherResponseDTO response = weatherService.getWeatherForPincode(request.getPincode(), date);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all weather records", description = "Fetch all weather records with pagination.")
    @GetMapping
    public ResponseEntity<Page<WeatherResponseDTO>> getAllWeatherRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {


        Page<WeatherResponseDTO> response = weatherService.findAllWeatherRecord(page, size);
        return ResponseEntity.ok(response);
    }
}
