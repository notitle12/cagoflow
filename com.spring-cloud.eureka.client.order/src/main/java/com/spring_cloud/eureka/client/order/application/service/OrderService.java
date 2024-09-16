package com.spring_cloud.eureka.client.order.application.service;

import com.spring_cloud.eureka.client.order.application.dtos.OrderDto;
import com.spring_cloud.eureka.client.order.application.exception.exceptionsdefined.DoNotCheckOterDataEception;
import com.spring_cloud.eureka.client.order.application.exception.exceptionsdefined.TryAgainLaterException;
import com.spring_cloud.eureka.client.order.application.feignclient.client.CompanyClient;
import com.spring_cloud.eureka.client.order.domain.repository.OrderRepository;
import com.spring_cloud.eureka.client.order.domain.service.OrderDomainService;
import com.spring_cloud.eureka.client.order.presentaion.dtos.RequestOrderDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.CompanyIdAndStockInfoDto;
import org.example.HubInformationFromCompanyDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "order service")
public class OrderService {

    private final OrderDomainService orderDomainService;
    private final CompanyClient companyClient;
    private final OrderRepository orderRepository;

    /**
     * 주문 등록
     * @param orderDto 주문 정보
     * @param userId 주문 등록자
     */
    @Transactional(timeout = 10)
    public void createOrderService(RequestOrderDto orderDto, Long userId) {
        // 1. company 서비스로 상품의 공급업체 id랑 상품의 재고를 가져옴
        try{
            CompanyIdAndStockInfoDto productinfo = companyClient.findById(orderDto.getProductId());
            // 1-1. 주문한 상품이 업체의 상품인지 확인
            if (!productinfo.getConpanyId().equals(orderDto.getSupplierId())) {
                throw new IllegalArgumentException("업체에 없는 상품입니다.");
            }
            // 1-2. 주문수량이 재고 보다 적은지 확인
            if(!(productinfo.getQuantity()>orderDto.getQuantity())){
                throw new IllegalArgumentException("재고가 모자랍니다.");
            }
        }catch (FeignException.NotFound e){
            // 404 응답
            throw new IllegalArgumentException("상품이 없습니다.");
        }catch (FeignException.BadGateway e){
            // 400 응답
            throw new TryAgainLaterException("나중에 다시 시도해주세요");
        }catch (FeignException e){
            throw e;
        }

        // 2. 상품의 재고를 차감 요청 -> rabbitMQ로 대채 가능
        try{
            companyClient.reduceInventoryRequest(orderDto.getProductId(), orderDto.getQuantity());
        }catch (FeignException.BadGateway e){
            throw new TryAgainLaterException("나중에 다시 시도해주세요");
        }catch (FeignException e){
            throw new TryAgainLaterException("나중에 다시 시도해주세요");
        }

        try{
            // 3. 주문의 공급업체, 수령업체 id로 company service로 업체의 소속허브 id, 업체 주소정보 가져오기
            HubInformationFromCompanyDTO hubInfo = companyClient.getHudId(orderDto.getSupplierId(), orderDto.getReceiverId());
            // 4. 주문 저장 [도메인 서비스]
            orderDomainService.saveOrder(orderDto, hubInfo, userId);
        }catch (Exception e) {
            // 주문 저장 도중 예외 발생 시 재고를 원상 복구하는 로직
            try {
                companyClient.restoreInventoryRequest(orderDto.getProductId(), orderDto.getQuantity());
            } catch (Exception restoreEx) {
                // 복구 실패 시 추가 로직 처리 (예: 알림, 로그 등)
                log.error("productId: {} quantity: {} 재고 롤백 실패", orderDto.getProductId(), orderDto.getQuantity());
                throw new TryAgainLaterException("나중에 다시 시도해주세요");
            }
            throw new TryAgainLaterException("주문 처리 중 오류 발생. 재고는 복구되었습니다.");
        }
;


    }

    /**
     * 주문조회 상세
     * @param orderId
     * @param userId
     * @return
     */
    public OrderDto findOrderInfo(UUID orderId, Long userId) {
        // 1. 주문 id로 주문내용 가져오기
        OrderDto orderDto = orderRepository.findById(orderId).map(OrderDto :: of)
                .orElseThrow(() -> new IllegalArgumentException("주문이 없습니다."));
        // 2. 조회자와 조회한 주문내역서의 주문자와 같은 지 확인
        if(!(orderDto.getUserId() == userId)){
            throw new DoNotCheckOterDataEception("타인의 데이터는 볼 수 없습니다.");
        }
        return orderDto;
    }
}
