package com.spring_cloud.eureka.client.company.presentation.controller;

import com.spring_cloud.eureka.client.company.application.dto.ProductResponseDto;
import com.spring_cloud.eureka.client.company.application.service.ProductService;
import com.spring_cloud.eureka.client.company.presentation.request.ProductRequest;
import com.spring_cloud.eureka.client.company.presentation.request.ProductSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
}
