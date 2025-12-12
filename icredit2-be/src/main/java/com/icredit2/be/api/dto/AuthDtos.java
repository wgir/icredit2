package com.icredit2.be.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public class AuthDtos {

    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String password,
            @JsonProperty("company_domain") String companyDomain, // Using domain as easier identifier
            @JsonProperty("company_id") String companyId // Or ID
    ) {
    }

    public record AuthResponse(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("expires_in") long expiresIn,
            @JsonProperty("refresh_token") String refreshToken) {
    }

    public record RefreshTokenRequest(
            @JsonProperty("refresh_token") @NotBlank String refreshToken) {
    }

    public record LogoutRequest(
            @JsonProperty("refresh_token") String refreshToken) {
    }
}
