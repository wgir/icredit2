package com.icredit2.be.domain.repository;

import com.icredit2.be.domain.model.Company;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findByDomain(String domain);

    boolean existsByName(String name);

    boolean existsByDomain(String domain);
}
