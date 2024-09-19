package com.spring_cloud.eureka.client.message.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WeatherService {

    @Value("${spring.weather.api.url}")
    private String apiUrl;

    @Value("${spring.weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public String getWeatherData(String baseDate, String baseTime, String nx, String ny) {
        String url = String.format("%s?serviceKey=%s&pageNo=1&numOfRows=1000&dataType=JSON&base_date=%s&base_time=%s&nx=%s&ny=%s",
                apiUrl, apiKey, baseDate, baseTime, nx, ny);

        return restTemplate.getForObject(url, String.class);
    }
}
