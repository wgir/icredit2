package com.icredit2.be.api.controller;

import com.icredit2.be.api.dto.RoleDtos;
import com.icredit2.be.domain.model.Role;
import com.icredit2.be.service.RoleService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/companies/{companyId}/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    // @PreAuthorize("hasAuthority('perm:create_role')") // Uncomment when
    // permissions are defined
    public ResponseEntity<RoleDtos.RoleResponse> createRole(
            @PathVariable UUID companyId,
            @RequestBody RoleDtos.CreateRoleRequest request) {
        // TODO: Verify currentUser.companyId == companyId
        Role role = roleService.createRole(companyId, request.name(), request.permissions());
        return ResponseEntity.ok(new RoleDtos.RoleResponse(role.getId(), role.getName(), role.getPermissions()));
    }

    @GetMapping
    public ResponseEntity<List<RoleDtos.RoleResponse>> listRoles(@PathVariable UUID companyId) {
        return ResponseEntity.ok(roleService.getRoles(companyId).stream()
                .map(r -> new RoleDtos.RoleResponse(r.getId(), r.getName(), r.getPermissions()))
                .collect(Collectors.toList()));
    }
}
