package com.spring_cloud.eureka.client.company.presentation.controller;


import com.spring_cloud.eureka.client.company.application.dto.CompanyResponseDto;
import com.spring_cloud.eureka.client.company.application.service.CompanyService;
import com.spring_cloud.eureka.client.company.presentation.request.CompanyRequest;
import com.spring_cloud.eureka.client.company.presentation.request.CompanySearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<Void> createCompany(@RequestBody CompanyRequest companyRequest) {
        companyService.createCompany(companyRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponseDto> getCompanyById(@PathVariable("companyId") UUID companyId) {
        return ResponseEntity.ok(companyService.getCompanyById(companyId));
    }

    @PutMapping("/{companyId}")
    public ResponseEntity<Void> updateCompany(@PathVariable UUID companyId,
                                              @RequestBody CompanyRequest companyRequest) {
        companyService.updateCompany(companyId, companyRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteCompany(@PathVariable UUID companyId) {
        companyService.deleteCompany(companyId, "");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CompanyResponseDto>> searchCompany(
            @ModelAttribute CompanySearch companySearch) {
        return ResponseEntity.ok(companyService.searchCompany(companySearch));
    }


}
