package com.spring_cloud.eureka.client.company.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring_cloud.eureka.client.company.domain.enums.SortType;
import com.spring_cloud.eureka.client.company.domain.model.Company;
import com.spring_cloud.eureka.client.company.domain.model.QCompany;
import com.spring_cloud.eureka.client.company.domain.repository.CompanyRepositoryCustom;
import com.spring_cloud.eureka.client.company.presentation.request.CompanySearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CompanyRepositoryImpl implements CompanyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Company> searchCompany(CompanySearch companySearch, Pageable pageable) {
        QCompany company = QCompany.company;

        BooleanBuilder whereClause = new BooleanBuilder();

        // 회사 이름 조건
        if (companySearch.getCompanyName() != null) {
            whereClause.and(company.companyName.containsIgnoreCase(companySearch.getCompanyName()));
        }

        // 회사 타입 조건
        if (companySearch.getCompanyType() != null) {
            whereClause.and(company.companyType.eq(companySearch.getCompanyType()));
        }

        // 회사 주소 조건
        if (companySearch.getCompanyAddress() != null) {
            whereClause.and(company.companyAddress.containsIgnoreCase(companySearch.getCompanyAddress()));
        }

        // 정렬 조건 적용
        OrderSpecifier<?> sortOrder = orderBy(companySearch.getSortBy(), companySearch.getAscending());

        // 동적 쿼리 실행
        List<Company> results = queryFactory
                .selectFrom(company)
                .where(whereClause)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sortOrder)
                // 정렬 설정
                .fetch();

        // 전체 검색 결과 수를 위한 count 쿼리
        long total = queryFactory
                .selectFrom(company)
                .where(whereClause)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    private OrderSpecifier<?> orderBy(SortType sortType, Boolean asc) {
        QCompany company = QCompany.company;
        com.querydsl.core.types.Order inOrder = asc ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC;
        return switch (sortType) {
            case CREATED_AT -> new OrderSpecifier<>(inOrder, company.createdAt);
            case UPDATED_AT -> new OrderSpecifier<>(inOrder, company.updatedAt);
        };
    }
}
