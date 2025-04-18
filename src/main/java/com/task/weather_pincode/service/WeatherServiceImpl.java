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
    WeatherRecordRepository repository;

    @Autowired
    GeoCodingService geoCodingService;

    @Autowired
    WeatherApiService weatherApiService;

    @Override
    public WeatherResponseDTO getWeatherForPincode(String pincode, LocalDate forDate) {
        Optional<WeatherRecord> existingRecord = repository.findByPincodeAndRecordDate(pincode, forDate);
        if (existingRecord.isPresent()) {
            return new WeatherResponseDTO(
                    existingRecord.get().getPincode(),
                    existingRecord.get().getRecordDate().toString(),
                    existingRecord.get().getWeatherData()
            );
        }

        Map<String, String> coordinates = geoCodingService.getLatLongFromPincode(pincode);
        String latitude = coordinates.get("lat");
        String longitude = coordinates.get("lng");

        String weatherData = weatherApiService.getWeatherData(latitude, longitude);

        WeatherRecord record = new WeatherRecord(pincode, latitude, longitude, weatherData, forDate);
        repository.save(record);

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
