package com.spring_cloud.eureka.client.message.domain;

import com.spring_cloud.eureka.client.message.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "slack_message")
public class SlackMessage extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    @Column(name = "message", nullable = false)
    private String message;

}
