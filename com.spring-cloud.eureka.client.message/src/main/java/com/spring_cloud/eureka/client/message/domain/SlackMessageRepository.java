package com.spring_cloud.eureka.client.message.domain;

import com.spring_cloud.eureka.client.message.domain.SlackMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SlackMessageRepository extends JpaRepository<SlackMessage, UUID> {

}
