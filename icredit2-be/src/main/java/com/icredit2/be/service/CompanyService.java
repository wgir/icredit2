package com.icredit2.be.service;

import com.icredit2.be.api.dto.CompanyDtos;
import com.icredit2.be.domain.model.Company;
import com.icredit2.be.domain.repository.CompanyRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyDtos.CompanyResponse getCompany(UUID id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new com.icredit2.be.api.exception.ResourceNotFoundException("Company not found"));
        return mapToResponse(company);
    }

    private CompanyDtos.CompanyResponse mapToResponse(Company company) {
        return new CompanyDtos.CompanyResponse(
                company.getId(),
                company.getName(),
                company.getDomain(),
                company.getCreatedAt());
    }
}
