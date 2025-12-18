package com.icredit2.be.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDtos {

        public record LoginRequest(
                        @NotBlank @Email String email,
                        @NotBlank String password) {
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

        public record CurrentUserResponse(
                        @JsonProperty("user_name") String userName,
                        @JsonProperty("email") String email) {
        }
}
