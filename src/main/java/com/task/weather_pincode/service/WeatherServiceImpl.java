package com.task.weather_pincode.service;

import com.task.weather_pincode.dto.WeatherResponseDTO;
import com.task.weather_pincode.model.WeatherRecord;
import com.task.weather_pincode.repository.WeatherRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
public class WeatherServiceImpl  implements WeatherService{
    @Autowired
    private WeatherRecordRepository repository;

    @Autowired
    private GeoCodingService geoCodingService;

    @Autowired
    private WeatherApiService weatherApiService;

    @Override
    public WeatherResponseDTO getWeatherForPincode(String pincode, LocalDate forDate) {
        // ✅ Step 1: Check cache for existing data
        Optional<WeatherRecord> existingRecord = repository.findByPincodeAndRecordDate(pincode, forDate);
        if (existingRecord.isPresent()) {
            return new WeatherResponseDTO(
                    existingRecord.get().getPincode(),
                    existingRecord.get().getRecordDate().toString(),
                    existingRecord.get().getWeatherData()
            );
        }

        // ✅ Step 2: Get lat/long from geocoding service
        Map<String, String> coordinates = geoCodingService.getLatLongFromPincode(pincode);
        String latitude = coordinates.get("lat");
        String longitude = coordinates.get("lng");

        // ✅ Step 3: Get weather data from OpenWeather API
        String weatherData = weatherApiService.getWeatherData(latitude, longitude);

        // ✅ Step 4: Save data in database
        WeatherRecord record = new WeatherRecord(pincode, latitude, longitude, weatherData, forDate);
        repository.save(record);

        // ✅ Step 5: Return response
        return new WeatherResponseDTO(pincode, forDate.toString(), weatherData);
    }

    @Override
    public Page<WeatherResponseDTO> findAllWeatherRecord(int page ,int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<WeatherRecord> records = repository.findAll(pageable);
        Page<WeatherResponseDTO> response = records.map(
                record -> new WeatherResponseDTO(
                        record.getPincode(),
                        record.getRecordDate().toString(),
                        record.getWeatherData()
                )
        );
        return response;
    }
}
