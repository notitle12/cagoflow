package com.spring_cloud.eureka.client.message.presentation;

import com.spring_cloud.eureka.client.message.application.GeminiService;
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
    private final GeminiService geminiService;


    @Scheduled(cron = "0 0 6 * * *") // 매일 오전 6시에 실행
    public void fetchWeatherData() {
        String baseDate = getTodayDate();
        String baseTime = "0600"; // 오전 6시
        String nx = "55"; // 예시 좌표
        String ny = "127";

        String weatherData = weatherService.getWeatherData(baseDate, baseTime, nx, ny);

        String summary = geminiService.getContents(weatherData+" 이 정보를 요약해줘");

        SlackMessageReqDto slackMessageReqDto = new SlackMessageReqDto("slack-channel-id", summary);
        slackMessageService.sendMessage(slackMessageReqDto);


        System.out.println(weatherData);
    }

    private String getTodayDate() {
        // 오늘 날짜를 YYYYMMDD 형식으로 반환
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
