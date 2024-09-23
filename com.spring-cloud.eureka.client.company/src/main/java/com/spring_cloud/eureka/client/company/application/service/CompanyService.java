package com.spring_cloud.eureka.client.company.application.service;


import com.spring_cloud.eureka.client.company.application.dto.CompanyResponseDto;
import com.spring_cloud.eureka.client.company.domain.model.Company;
import com.spring_cloud.eureka.client.company.domain.service.CompanyDomainService;
import com.spring_cloud.eureka.client.company.application.client.HubClient;
import com.spring_cloud.eureka.client.company.application.client.HubResponse;
import com.spring_cloud.eureka.client.company.presentation.request.CompanyRequest;
import com.spring_cloud.eureka.client.company.presentation.request.CompanySearch;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.example.HubInformationFromCompanyDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyDomainService companyDomainService;
    private final HubClient hubClient;
    private final ProductService productService;

    public void createCompany(CompanyRequest companyRequest) {
        validateHubId(companyRequest.getHubId());

        companyDomainService.createCompany(
                companyRequest.getCompanyName(),
                companyRequest.getHubId(),
                companyRequest.getUserId(),
                companyRequest.getCompanyAddress(),
                companyRequest.getCompanyType()
        );
    }

    public void updateCompany(UUID companyId, CompanyRequest companyRequest) {
        validateHubId(companyRequest.getHubId());

        companyDomainService.updateCompany(
                companyId,
                companyRequest.getCompanyName(),
                companyRequest.getHubId(),
                companyRequest.getCompanyAddress(),
                companyRequest.getCompanyType()
        );
    }

    @Transactional
    public void deleteCompany(UUID companyId, String deleteBy) {
        companyDomainService.deleteCompany(companyId, deleteBy);
        productService.deleteProductsByCompanyId(companyId, deleteBy);
    }

    public CompanyResponseDto getCompanyById(UUID companyId) {
        Company company = companyDomainService.getCompanyById(companyId);
        return CompanyResponseDto.fromEntity(company);
    }

    private void validateHubId(UUID hubId) {
        try {
            HubResponse hubResponse = hubClient.getHubById(hubId);
        } catch (FeignException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 허브가 존재하지 않습니다.");
        } catch (FeignException.BadRequest e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "허브 ID가 유효하지 않습니다.");
        } catch (FeignException.GatewayTimeout e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "허브 서비스가 응답하지 않습니다. 나중에 다시 시도해 주세요.");
        } catch (FeignException.ServiceUnavailable e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "허브 서비스가 현재 사용할 수 없습니다.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "예기치 않은 오류가 발생했습니다.");
        }
    }


    public Page<CompanyResponseDto> searchCompany(CompanySearch companySearch) {
        return companyDomainService.searchCompany(companySearch).map(CompanyResponseDto::fromEntity);
    }

    public HubInformationFromCompanyDTO getHubIdCompanyAddress(UUID supplierId, UUID receiverId) {
        // 공급업체 정보 조회
        Company supplier = companyDomainService.getCompanyById(supplierId);

        // 수령업체 정보 조회
        Company receiver = companyDomainService.getCompanyById(receiverId);

        return new HubInformationFromCompanyDTO(
                supplier.getHubId(), supplier.getCompanyAddress(), receiver.getHubId(), receiver.getCompanyAddress());
    }
}
