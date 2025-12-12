package com.icredit2.be.service;

import com.icredit2.be.domain.model.Company;
import com.icredit2.be.domain.model.Role;
import com.icredit2.be.domain.repository.CompanyRepository;
import com.icredit2.be.domain.repository.RoleRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public Role createRole(UUID companyId, String name, List<String> permissions) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Role role = Role.builder()
                .company(company)
                .name(name)
                .permissions(permissions)
                .build();

        return roleRepository.save(role);
    }

    public List<Role> getRoles(UUID companyId) {
        return roleRepository.findByCompanyId(companyId);
    }
}
