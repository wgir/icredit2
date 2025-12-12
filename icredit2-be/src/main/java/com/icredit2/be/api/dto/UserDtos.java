package com.icredit2.be.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class UserDtos {

    public record UserResponse(
            UUID id,
            @JsonProperty("company_id") UUID companyId,
            String email,
            @JsonProperty("display_name") String displayName,
            @JsonProperty("is_verified") boolean isVerified,
            List<String> roles, // Role names or IDs
            @JsonProperty("created_at") LocalDateTime createdAt) {
    }
}
