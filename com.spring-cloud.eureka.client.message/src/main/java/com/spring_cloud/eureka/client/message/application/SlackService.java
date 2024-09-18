package com.spring_cloud.eureka.client.message.application;

import com.spring_cloud.eureka.client.message.presentation.SlackMessageReqDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SlackService {
    private final RestTemplate restTemplate;
    private final String slackUrl = "https://hooks.slack.com/services/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";

    public void sendSlackMessage(SlackMessageReqDto requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject json = new JSONObject();
        json.put("channel", requestDto.getReceiverId());
        json.put("text", requestDto.getMessage());

        HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);
        restTemplate.postForEntity(slackUrl, request, String.class);
    }
}
