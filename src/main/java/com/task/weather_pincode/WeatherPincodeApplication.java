package com.task.weather_pincode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class WeatherPincodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherPincodeApplication.class, args);
	}

}
