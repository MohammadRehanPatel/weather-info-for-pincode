package com.task.weather_pincode.service;

import com.task.weather_pincode.dto.WeatherResponseDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface WeatherService {
    WeatherResponseDTO getWeatherForPincode(String pincode, LocalDate forDate);
     Page<WeatherResponseDTO> findAllWeatherRecord(int page , int size);
}
