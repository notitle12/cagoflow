package com.spring_cloud.eureka.client.company.presentation.controller;


import com.spring_cloud.eureka.client.company.application.dto.CompanyResponseDto;
import com.spring_cloud.eureka.client.company.application.service.CompanyService;
import com.spring_cloud.eureka.client.company.presentation.request.CompanyRequest;
import com.spring_cloud.eureka.client.company.presentation.request.CompanySearch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.HubInformationFromCompanyDTO;
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

    @Operation(summary = "공급, 수령업체의 소속 허브 id와 주소")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "수령, 공급 업체 중 데이터가 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/api/products/reduce")
    public ResponseEntity<HubInformationFromCompanyDTO> getHudId( @RequestParam("supplierId") UUID supplierId,
                                                                  @RequestParam("receiverId") UUID receiverId){
        // Todo: RequestParam으로 들어오는 공급, 수령 업체의id를 가지고 업체의 주소와 소속 허브의id 정보 요철
        // Todo: 정상: 200번 응답과 데이터, 404번: 수령 공급업체 id 로 find시 둘중하나라도 데이터가 없을 때 , 500번: 조회시 문제 발생
        return null;
    }





}
