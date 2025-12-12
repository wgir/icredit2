package com.icredit2.be.api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL) Map<String, String> errors) {
}
