package com.spring_cloud.eureka.client.message.presentation.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlackMessageReqDto {
    private String receiverId;
    private String message;
}
