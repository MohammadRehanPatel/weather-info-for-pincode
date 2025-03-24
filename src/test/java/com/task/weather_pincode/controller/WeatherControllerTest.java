package com.task.weather_pincode.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.weather_pincode.dto.WeatherRequestDTO;
import com.task.weather_pincode.dto.WeatherResponseDTO;
import com.task.weather_pincode.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Autowired
    private ObjectMapper objectMapper;

    private WeatherRequestDTO requestDTO;
    private WeatherResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new WeatherRequestDTO();
        requestDTO.setPincode("411001");
        requestDTO.setForDate("2025-03-30");

        responseDTO = new WeatherResponseDTO(
                "411001",
                "2025-03-30",
                "Sunny 28°C"
        );
    }

    @Test
    void shouldGetWeatherSuccessfully() throws Exception {
        when(weatherService.getWeatherForPincode(anyString(), any(LocalDate.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pincode").value("411001"))
                .andExpect(jsonPath("$.weatherData").value("Sunny 28°C"));
    }

    @Test
    void shouldGetAllWeatherRecordsSuccessfully() throws Exception {
        Page<WeatherResponseDTO> mockPage = new PageImpl<>(List.of(responseDTO));
        when(weatherService.findAllWeatherRecord(anyInt(), anyInt()))
                .thenReturn(mockPage);

        mockMvc.perform(get("/api/weather?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].pincode").value("411001"))
                .andExpect(jsonPath("$.content[0].weatherData").value("Sunny 28°C"));
    }
}
