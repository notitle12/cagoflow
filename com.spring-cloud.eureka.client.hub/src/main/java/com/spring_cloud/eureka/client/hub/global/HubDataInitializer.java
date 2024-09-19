package com.spring_cloud.eureka.client.hub.global;

import com.spring_cloud.eureka.client.hub.application.service.HubService;
import com.spring_cloud.eureka.client.hub.domain.service.HubDomainService;
import com.spring_cloud.eureka.client.hub.presentation.dtos.HubRequestDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class HubDataInitializer implements CommandLineRunner {

    @Autowired
    private HubService hubService;
    @Autowired
    private HubDomainService hubDomainService;

    @Override
    public void run(String... args) throws Exception {
        if (hubDomainService.countHubs() == 0) { // 허브가 없는 경우에만 초기화
            createHub("서울특별시 센터", "서울특별시 송파구 송파대로 55", "055", 37.5086, 127.0744);
            createHub("경기 북부 센터", "경기도 고양시 덕양구 권율대로 570", "105", 37.6707, 126.9200);
            createHub("경기 남부 센터", "경기도 이천시 덕평로 257-21", "173", 37.2824, 127.4208);
            createHub("부산광역시 센터", "부산 동구 중앙대로 206", "456", 35.0682, 129.0184);
            createHub("대구광역시 센터", "대구 북구 태평로 161", "414", 35.8892, 128.6105);
            createHub("인천광역시 센터", "인천 남동구 정각로 29", "215", 37.4491, 126.7061);
            createHub("광주광역시 센터", "광주 서구 내방로 111", "619", 35.1603, 126.8526);
            createHub("대전광역시 센터", "대전 서구 둔산로 100", "352", 36.3504, 127.3845);
            createHub("울산광역시 센터", "울산 남구 중앙로 201", "446", 35.5395, 129.3114);
            createHub("세종특별자치시 센터", "세종특별자치시 한누리대로 2130", "301", 36.4800, 127.2892);
            createHub("강원특별자치도 센터", "강원특별자치도 춘천시 중앙로 1", "200", 37.8814, 127.7274);
            createHub("충청북도 센터", "충북 청주시 상당구 상당로 82", "283", 36.6417, 127.4893);
            createHub("충청남도 센터", "충남 홍성군 홍북읍 충남대로 21", "323", 36.6662, 126.6821);
            createHub("전북특별자치도 센터", "전북특별자치도 전주시 완산구 효자로 225", "550", 35.8201, 127.1538);
            createHub("전라남도 센터", "전남 무안군 삼향읍 오룡길 1", "585", 34.9552, 126.6138);
            createHub("경상북도 센터", "경북 안동시 풍천면 도청대로 455", "367", 36.5656, 128.7430);
            createHub("경상남도 센터", "경남 창원시 의창구 중앙대로 300", "514", 35.2286, 128.6797);
        }
    }

    private void createHub(String name, String address, String zipcode, double latitude, double longitude) {
        HubRequestDTO hubRequestDTO = HubRequestDTO.builder()
                .name(name)
                .address(address)
                .zipcode(zipcode)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        hubService.createHub(hubRequestDTO);
    }
}
