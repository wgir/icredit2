package com.icredit2.be.api.controller;

import com.icredit2.be.api.dto.AuthDtos;
import com.icredit2.be.api.dto.CompanyDtos;
import com.icredit2.be.security.CustomUserDetails;
import com.icredit2.be.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/v1/companies/register")
    public ResponseEntity<CompanyDtos.CompanyRegistrationResponse> registerCompany(
            @RequestBody @Valid CompanyDtos.CompanyRegistrationRequest request,
            HttpServletResponse response) {
        CompanyDtos.CompanyRegistrationResponse registrationResponse = authService.registerCompany(request);

        setJwtCookie(response, registrationResponse.tokens().accessToken(),
                registrationResponse.tokens().expiresIn() / 1000);

        return ResponseEntity.status(HttpStatus.CREATED).body(registrationResponse);
    }

    @PostMapping("/v1/auth/login")
    public ResponseEntity<AuthDtos.AuthResponse> login(
            @RequestBody @Valid AuthDtos.LoginRequest request,
            HttpServletResponse response) {
        AuthDtos.AuthResponse authResponse = authService.login(request);

        setJwtCookie(response, authResponse.accessToken(), authResponse.expiresIn() / 1000);

        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/v1/auth/me")
    public ResponseEntity<AuthDtos.CurrentUserResponse> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Cast to CustomUserDetails to access the User object
        if (userDetails instanceof CustomUserDetails customUserDetails) {
            AuthDtos.CurrentUserResponse response = new AuthDtos.CurrentUserResponse(
                    customUserDetails.getUser().getDisplayName(),
                    customUserDetails.getUser().getEmail());
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/v1/auth/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        setJwtCookie(response, "", 0);

        return ResponseEntity.ok().build();
    }

    private void setJwtCookie(HttpServletResponse response, String accessToken, long maxAgeSeconds) {
        Cookie jwtCookie = new Cookie("jwt", accessToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false); // TODO: Set to true in production
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge((int) maxAgeSeconds);
        response.addCookie(jwtCookie);
    }
}
