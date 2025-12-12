package com.icredit2.be.api.controller;

import com.icredit2.be.api.dto.CompanyDtos;
import com.icredit2.be.service.CompanyService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyDtos.CompanyResponse> getCompany(@PathVariable UUID companyId) {
        // TODO: Validate auth user belongs to companyId
        return ResponseEntity.ok(companyService.getCompany(companyId));
    }
}
