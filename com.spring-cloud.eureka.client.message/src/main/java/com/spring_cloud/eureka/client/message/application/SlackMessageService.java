package com.spring_cloud.eureka.client.message.application;

import com.spring_cloud.eureka.client.message.domain.SlackMessage;
import com.spring_cloud.eureka.client.message.domain.SlackMessageRepository;
import com.spring_cloud.eureka.client.message.presentation.dtos.SlackMessageReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlackMessageService {
    private final SlackMessageRepository slackMessageRepository;
    private final SlackService slackService;

    public void sendMessage(SlackMessageReqDto slackMessageReqDto) {

        //메시지 발송
        slackService.sendSlackMessage(slackMessageReqDto);

        //저장
        SlackMessage slackMessage = new SlackMessage();
        slackMessage.setReceiverId(slackMessageReqDto.getReceiverId());
        slackMessage.setMessage(slackMessageReqDto.getMessage());

        slackMessageRepository.save(slackMessage);
    }
}
