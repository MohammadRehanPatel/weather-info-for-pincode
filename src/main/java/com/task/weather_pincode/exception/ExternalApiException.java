package com.task.weather_pincode.exception;

public class ExternalApiException extends RuntimeException{
    public ExternalApiException(String message) {
        super(message);
    }
}
