package com.icredit2.be.api.controller;

import com.icredit2.be.api.dto.AuthDtos;
import com.icredit2.be.api.dto.CompanyDtos;
import com.icredit2.be.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/v1/companies/register")
    public ResponseEntity<CompanyDtos.CompanyRegistrationResponse> registerCompany(
            @RequestBody @Valid CompanyDtos.CompanyRegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerCompany(request));
    }

    @PostMapping("/v1/auth/login")
    public ResponseEntity<AuthDtos.AuthResponse> login(
            @RequestBody @Valid AuthDtos.LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
