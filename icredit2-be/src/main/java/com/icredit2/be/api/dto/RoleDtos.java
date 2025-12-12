package com.icredit2.be.api.dto;

import java.util.List;
import java.util.UUID;

public class RoleDtos {
    public record RoleResponse(
            UUID id,
            String name,
            List<String> permissions) {
    }

    public record CreateRoleRequest(
            String name,
            List<String> permissions) {
    }
}
