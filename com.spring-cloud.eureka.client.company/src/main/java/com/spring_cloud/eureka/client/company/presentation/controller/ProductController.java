package com.spring_cloud.eureka.client.company.presentation.controller;

import com.spring_cloud.eureka.client.company.application.dto.ProductResponseDto;
import com.spring_cloud.eureka.client.company.application.service.ProductService;
import com.spring_cloud.eureka.client.company.presentation.request.ProductRequest;
import com.spring_cloud.eureka.client.company.presentation.request.ProductSearch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
        return ResponseEntity.created(URI.create("/api/products")).build();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable("productId") UUID productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(@PathVariable UUID productId, @RequestBody ProductRequest productRequest) {
        productService.updateProduct(productId, productRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId, @RequestParam String deletedBy) {
        productService.deleteProduct(productId, deletedBy);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDto>> searchProducts(ProductSearch productSearch) {
        Page<ProductResponseDto> products = productService.searchProducts(productSearch);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "상품 차감 ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "차감 성공"),
            @ApiResponse(responseCode = "404", description = "해당 상품이 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/reduce")
    public ResponseEntity<?> reduceInventoryRequest(@RequestParam("productId") UUID productId,
                                                    @RequestParam("quantity") int quantity) {
        try {
            productService.reduceInventory(productId, quantity);
            return ResponseEntity.ok("재고 차감 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 상품이 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }


    @Operation(summary = "주문 처리중 문재 발생하여 보상트랜잭션으로 다시 재고 증가 ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재고 복구 성공"),
            @ApiResponse(responseCode = "404", description = "해당 상품이 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/restore")
    public ResponseEntity<?> restoreInventoryRequest(@RequestParam("productId") UUID productId,
                                                     @RequestParam("quantity") int quantity) {
        try {
            productService.restoreInventory(productId, quantity);
            return ResponseEntity.ok("재고 복구 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 상품이 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }
}
