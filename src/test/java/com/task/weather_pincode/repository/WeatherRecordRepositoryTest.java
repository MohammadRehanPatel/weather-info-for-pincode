package com.task.weather_pincode.repository;

import com.task.weather_pincode.model.WeatherRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class WeatherRecordRepositoryTest {

    @Autowired
    private WeatherRecordRepository repository;

    @Test
    void testSaveAndFindByPincodeAndRecordDate() {
        WeatherRecord record = new WeatherRecord(
                "411014",
                "18.5204",
                "73.8567",
                "{\"temp\":30, \"humidity\":50}",
                LocalDate.now()
        );

        repository.save(record);

        Optional<WeatherRecord> found = repository.findByPincodeAndRecordDate("411014", LocalDate.now());
        assertTrue(found.isPresent());
        assertEquals("411014", found.get().getPincode());
    }
}
