package com.icredit2.be.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public class CompanyDtos {

        public record CompanyRegistrationRequest(
                        @NotBlank String name,
                        @NotNull @Valid CompanyOwnerRequest owner) {
        }

        public record CompanyOwnerRequest(
                        @NotBlank @Email String email,
                        @NotBlank String password,
                        @JsonProperty("display_name") @NotBlank String displayName) {
        }

        public record CompanyResponse(
                        UUID id,
                        String name,
                        @JsonProperty("created_at") LocalDateTime createdAt) {
        }

        public record CompanyRegistrationResponse(
                        CompanyResponse company,
                        UserDtos.UserResponse owner,
                        AuthDtos.AuthResponse tokens) {
        }
}
