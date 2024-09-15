package com.spring_cloud.eureka.client.hub.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring_cloud.eureka.client.hub.domain.model.Hub;
import com.spring_cloud.eureka.client.hub.domain.model.QHub;
import com.spring_cloud.eureka.client.hub.domain.repository.HubRepositoryCustom;
import com.spring_cloud.eureka.client.hub.presentation.dtos.HubRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class HubRepositoryImpl implements HubRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Hub> findHubs(HubRequestDTO hubRequestDTO, Pageable pageable) {
        QHub hub = QHub.hub;

        BooleanBuilder whereBuilder = new BooleanBuilder(); // 동적으로 조건 추가할 수 있게 해주는 객체

        // 검색 조건 추가
        if (hubRequestDTO.getName() != null && !hubRequestDTO.getName().isEmpty()) {
            whereBuilder.and(hub.name.containsIgnoreCase(hubRequestDTO.getName()));
        }
        if (hubRequestDTO.getZipcode() != null && !hubRequestDTO.getZipcode().isEmpty()) {
            whereBuilder.and(hub.zipcode.eq(hubRequestDTO.getZipcode()));
        }
        if (hubRequestDTO.getAddress() != null && !hubRequestDTO.getAddress().isEmpty()) {
            whereBuilder.and(hub.address.containsIgnoreCase(hubRequestDTO.getAddress()));
        }
        if (hubRequestDTO.getLatitude() != null) {
            whereBuilder.and(hub.latitude.eq(hubRequestDTO.getLatitude()));
        }
        if (hubRequestDTO.getLongitude() != null) {
            whereBuilder.and(hub.longitude.eq(hubRequestDTO.getLongitude()));
        }

        // 쿼리 실행
        QueryResults<Hub> results = queryFactory
                .selectFrom(hub)
                .where(whereBuilder) // 조건 적용
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Hub> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }
}
