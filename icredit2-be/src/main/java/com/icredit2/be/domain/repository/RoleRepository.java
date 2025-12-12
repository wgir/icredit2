package com.icredit2.be.domain.repository;

import com.icredit2.be.domain.model.Role;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    List<Role> findByCompanyId(UUID companyId);
}
