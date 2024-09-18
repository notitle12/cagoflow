package com.spring_cloud.eureka.client.message.presentation;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlackMessageReqDto {
    private Long receiverId;
    private String message;
}
