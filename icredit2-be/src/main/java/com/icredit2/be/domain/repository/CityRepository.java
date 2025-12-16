package com.icredit2.be.domain.repository;

import com.icredit2.be.domain.model.City;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, UUID> {

    List<City> findByCompanyId(UUID companyId);

    Optional<City> findByIdAndCompanyId(UUID id, UUID companyId);

    boolean existsByNameAndCompanyId(String name, UUID companyId);
}
