package com.spring_cloud.eureka.client.company.presentation.controller;


import com.spring_cloud.eureka.client.company.application.service.CompanyService;
import com.spring_cloud.eureka.client.company.presentation.request.CompanyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<Void> createCompany(@RequestBody CompanyRequest companyRequest) {

        return ResponseEntity.ok().build();
    }

}
