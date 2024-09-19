package com.spring_cloud.eureka.client.message.presentation;

import com.spring_cloud.eureka.client.message.application.SlackMessageService;
import com.spring_cloud.eureka.client.message.application.WeatherService;
import com.spring_cloud.eureka.client.message.presentation.dtos.SlackMessageReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class WeatherScheduler {

    private final WeatherService weatherService;
    private final SlackMessageService slackMessageService;

    @Scheduled(cron = "0 0 6 * * *") // 매일 오전 6시에 실행
    public void fetchWeatherData() {
        String baseDate = getTodayDate();
        String baseTime = "0600"; // 오전 6시
        String nx = "55"; // 예시 좌표
        String ny = "127";

        String weatherData = weatherService.getWeatherData(baseDate, baseTime, nx, ny);

        //todo: 슬랙 메시지 발송
//        SlackMessageReqDto slackMessageReqDto = new SlackMessageReqDto("slack-channel-id", weatherData);
//        slackMessageService.sendMessage("slack-channel-id", weatherData);

        System.out.println(weatherData);
    }

    private String getTodayDate() {
        // 오늘 날짜를 YYYYMMDD 형식으로 반환
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
