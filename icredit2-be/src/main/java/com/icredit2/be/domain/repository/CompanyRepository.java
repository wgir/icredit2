package com.icredit2.be.domain.repository;

import com.icredit2.be.domain.model.Company;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    boolean existsByName(String name);
}
