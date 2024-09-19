package com.spring_cloud.eureka.client.company.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring_cloud.eureka.client.company.domain.enums.SortType;
import com.spring_cloud.eureka.client.company.domain.model.Product;
import com.spring_cloud.eureka.client.company.domain.model.QCompany;
import com.spring_cloud.eureka.client.company.domain.model.QProduct;
import com.spring_cloud.eureka.client.company.domain.repository.ProductRepositoryCustom;
import com.spring_cloud.eureka.client.company.presentation.request.ProductSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> searchProducts(ProductSearch productSearch, Pageable pageable) {
        QProduct product = QProduct.product;
        QCompany company = QCompany.company;

        BooleanBuilder whereClause = new BooleanBuilder();

        // 상품 이름 조건
        if (productSearch.getProductName() != null) {
            whereClause.and(product.productName.containsIgnoreCase(productSearch.getProductName()));
        }

        // 회사 이름 조건
        if (productSearch.getCompanyId() != null) {
            whereClause.and(product.companyId.eq(productSearch.getCompanyId()));
        }

        // 삭제되지 않은 상품만 조회
        whereClause.and(product.isDelete.eq(false));

        // 정렬 조건 적용
        OrderSpecifier<?> sortOrder = orderBy(productSearch.getSortBy(), productSearch.getAscending());

        // 동적 쿼리 실행
        List<Product> results = queryFactory
                .selectFrom(product)
                .where(whereClause)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sortOrder)
                .fetch();

        // 전체 검색 결과 수를 위한 count 쿼리
        long total = queryFactory
                .selectFrom(product)
                .where(whereClause)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    private OrderSpecifier<?> orderBy(SortType sortType, Boolean asc) {
        QProduct product = QProduct.product;
        com.querydsl.core.types.Order inOrder = asc ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC;
        return switch (sortType) {
            case CREATED_AT -> new OrderSpecifier<>(inOrder, product.createdAt);
            case UPDATED_AT -> new OrderSpecifier<>(inOrder, product.updatedAt);
        };
    }
}
