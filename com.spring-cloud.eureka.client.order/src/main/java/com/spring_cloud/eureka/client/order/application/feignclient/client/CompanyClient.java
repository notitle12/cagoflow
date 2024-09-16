package com.spring_cloud.eureka.client.order.application.feignclient.client;

import org.example.CompanyIdAndStockInfoDto;
import org.example.HubInformationFromCompanyDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;
@Service
@FeignClient(name = "CompanyService")
public interface CompanyClient {

    @GetMapping("company/product/{id}")
    CompanyIdAndStockInfoDto findById(@PathVariable("id") UUID id);

    @PutMapping("company/product/reduce")
    void reduceInventoryRequest( @RequestParam("productId") UUID productId,
                                 @RequestParam("quantity") int quantity);

    @GetMapping("company/hub")
    HubInformationFromCompanyDTO getHudId( @RequestParam("supplierId") UUID supplierId,
                                           @RequestParam("receiverId") UUID receiverId);

    @PutMapping("company/product/restore")
    void restoreInventoryRequest( @RequestParam("productId") UUID productId,
                                  @RequestParam("quantity") int quantity);
}
