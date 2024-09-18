package com.spring_cloud.eureka.client.message.presentation.controller;

import com.spring_cloud.eureka.client.message.application.SlackMessageService;
import com.spring_cloud.eureka.client.message.presentation.dtos.SlackMessageReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/slack")
@RequiredArgsConstructor
public class SlackMessageController {

    private final SlackMessageService slackMessageService;

    // 슬랙 메시지 발송 요청을 받는 엔드포인트
    @PostMapping("/send")
    public ResponseEntity<String> sendSlackMessage(@RequestBody SlackMessageReqDto slackMessageReqDto) {
        try {
            // 메시지를 발송하고 저장하는 로직
            slackMessageService.sendMessage(slackMessageReqDto);
            return ResponseEntity.ok("Slack message sent and saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send Slack message: " + e.getMessage());
        }
    }
}
