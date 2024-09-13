package com.spring_cloud.eureka.client.company.domain.service;

import com.spring_cloud.eureka.client.company.domain.model.Product;
import com.spring_cloud.eureka.client.company.domain.repository.ProductRepository;
import com.spring_cloud.eureka.client.company.domain.repository.CompanyRepository;
import com.spring_cloud.eureka.client.company.presentation.request.ProductSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductDomainService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public void createProduct(String productName, Integer productQuantity, UUID companyId, UUID hubId) {
        validateCompanyExists(companyId);
        Product product = Product.create(productName, productQuantity, companyId, hubId);
        productRepository.save(product);
    }

    @Transactional
    public void updateProduct(UUID productId, String productName, Integer productQuantity, UUID companyId, UUID hubId) {
        validateCompanyExists(companyId);
        Product product = getProductById(productId);
        product.update(productName, productQuantity, companyId, hubId);
        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(UUID productId, String deleteBy) {
        Product product = getProductById(productId);
        product.deleteSoftly(deleteBy);
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Product getProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }

    // 회사 존재 여부 검증
    private void validateCompanyExists(UUID companyId) {
        if (!companyRepository.existsById(companyId)) {
            throw new IllegalArgumentException("존재하지 않는 업체입니다.");
        }
    }

    @Transactional(readOnly = true)
    public Page<Product> searchProducts(ProductSearch productSearch) {
        Sort sort = productSearch.getAscending()
                ? Sort.by(Sort.Direction.ASC, productSearch.getSortBy().getValue())
                : Sort.by(Sort.Direction.DESC, productSearch.getSortBy().getValue());

        Pageable pageable = PageRequest.of(productSearch.getPage(), productSearch.getSize(), sort);
        return productRepository.searchProducts(productSearch, pageable);
    }
}
