package com.spring_cloud.eureka.client.order.presentaion.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * 주문 요청시 사용하는 dto
 * 상품 id, 공급업체 id, 수령업체 id, 수량
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestOrderDto {

    @NotNull(message = "상품 정보가 없습니다.")
    @NotBlank(message = "상품 정보가 없습니다.")
    private UUID productId;

    @NotNull(message = "공급 업체를 입력해주세요")
    @NotBlank(message = "공급 업체를 입력해주세요")
    private UUID supplierId;

    @NotNull(message = "수령 업체를 입력해주세요")
    @NotBlank(message = "수령 업체를 입력해주세요")
    private UUID receiverId;

    @NotNull(message = "수량을 입력해주세요")
    @NotBlank(message = "수량을 입력해주세요")
    @Min(value = 1, message = "최소 수량 이상을 입력해주세요")
    private int quantity;
}
