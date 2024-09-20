package com.spring_cloud.eureka.client.order.infrastructure;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring_cloud.eureka.client.order.application.dtos.QResponseOrderInfoDto;
import com.spring_cloud.eureka.client.order.application.dtos.ResponseOrderInfoDto;
import com.spring_cloud.eureka.client.order.domain.model.order.Order;
import com.spring_cloud.eureka.client.order.domain.model.order.OrderState;
import com.spring_cloud.eureka.client.order.domain.model.order.QOrder;
import com.spring_cloud.eureka.client.order.presentaion.dtos.RequestSearchOrderDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static com.spring_cloud.eureka.client.order.domain.model.order.QOrder.order;
import static org.springframework.util.StringUtils.hasText;
@Repository
public class OrderRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryCustom(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    public Page<ResponseOrderInfoDto> findAllOrder(Pageable pageable, RequestSearchOrderDto condition){
        JPAQuery<ResponseOrderInfoDto> query = queryFactory
                .select(new QResponseOrderInfoDto(
                        order.id,
                        order.productId,
                        order.supplierId,
                        order.supplierHubId,
                        order.receiverId,
                        order.receiverHubId,
                        order.quantity,
                        order.userId,
                        order.orderState
                ))
                .from(order)
                .where();
        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            sort.forEach(order -> {
                String property = order.getProperty();
                Sort.Direction direction = order.getDirection();

                if ("createdAt".equals(property)) {
                    query.orderBy(direction.isAscending() ? QOrder.order.createdAt.asc() : QOrder.order.createdAt.desc());
                } else if ("updatedAt".equals(property)) {
                    query.orderBy(direction.isAscending() ? QOrder.order.updatedAt.asc() : QOrder.order.updatedAt.desc());
                } else {
                    query.orderBy(direction.isAscending() ? QOrder.order.createdAt.asc() : QOrder.order.createdAt.desc());
                }
            });
        }
        List<ResponseOrderInfoDto> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .where(supplierEq(condition.getSupplierId())
                        .or(receiverEq(condition.getReceiverId()))
                        .or(orderStateEq(condition.getOrderState()))
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }
    private BooleanExpression supplierEq(UUID supplierId) {
        return hasText(supplierId.toString()) ? order.supplierId.eq(supplierId) : null;
    }
    private BooleanExpression receiverEq(UUID receiverId) {
        return hasText(receiverId.toString()) ? order.receiverId.eq(receiverId) : null;
    }
    private BooleanExpression orderStateEq(OrderState state) {
        return hasText(state.getValue()) ? order.orderState.eq(state) : null;
    }
}
