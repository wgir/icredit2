package com.icredit2.be.domain.repository;

import com.icredit2.be.domain.model.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailAndCompanyId(String email, UUID companyId);

    boolean existsByEmailAndCompanyId(String email, UUID companyId);

    List<User> findByCompanyId(UUID companyId);

    boolean existsByEmail(String email);
}
