package com.icredit2.be.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;

public class CityDtos {

        public record CityRequest(
                        @NotBlank(message = "Name is required") String name,
                        boolean active) {
        }

        public record CityResponse(
                        UUID id,
                        String name, boolean active,
                        LocalDateTime createdAt,
                        LocalDateTime updatedAt) {
        }
}
