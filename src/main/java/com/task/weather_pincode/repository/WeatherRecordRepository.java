package com.task.weather_pincode.repository;

import com.task.weather_pincode.model.WeatherRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface WeatherRecordRepository extends JpaRepository<WeatherRecord,Long> {
    Optional<WeatherRecord> findByPincodeAndRecordDate(String pincode, LocalDate recordDate);

    @Query("SELECT w FROM WeatherRecord w ORDER BY w.recordDate DESC")
    Page<WeatherRecord> findAll(Pageable pageable);
}
