package com.spring_cloud.eureka.client.message.application;

import com.spring_cloud.eureka.client.message.global.exception.GeminiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${spring.gemini.api.url}")
    private String apiUrl;

    @Value("${spring.gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate;

    public GeminiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getContents(String prompt) {
        String requestUrl = apiUrl + "?key=" + geminiApiKey;

        try {
            // 요청 데이터를 Map으로 간단하게 처리
            Map<String, String> request = new HashMap<>();
            request.put("prompt", prompt);

            // Gemini API로 POST 요청을 보내고, 응답을 Map으로 받음
            Map<String, Object> response = restTemplate.postForObject(requestUrl, request, Map.class);

            // 응답에서 첫 번째 후보 메시지 추출
            if (response != null && response.containsKey("candidates")) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
                if (!candidates.isEmpty()) {
                    Map<String, Object> firstCandidate = candidates.get(0);
                    Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");
                    List<Map<String, String>> parts = (List<Map<String, String>>) content.get("parts");
                    return parts.get(0).get("text");
                }
            }

            // 응답에 후보가 없을 경우 예외 처리
            throw new GeminiException("No candidates found in the response");

        } catch (RestClientException e) {
            // RestTemplate에서 발생하는 예외 처리
            throw new GeminiException("Failed to call Gemini API", e);
        }
    }
}
