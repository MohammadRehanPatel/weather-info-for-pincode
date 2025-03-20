package com.task.weather_pincode.service;

import java.util.Map;

public interface GeoCodingService {
    Map<String, String> getLatLongFromPincode(String pincode);
}
