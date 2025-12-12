package com.icredit2.be.security;

import com.icredit2.be.domain.model.Company;
import com.icredit2.be.domain.model.User;
import com.icredit2.be.domain.repository.CompanyRepository;
import com.icredit2.be.domain.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Expected format: "email|companyIdOrDomain" or just "email" if strictly unique
        // globally (not guaranteed)
        // OR: This method is called with UserID (stringified UUID) during JWT Auth if
        // we treat sub as username.

        // Strategy:
        // 1. If username looks like a UUID, try to load by ID (for JWT flow).
        // 2. If username has a pipe '|', split into email and companyIdentifier (for
        // Login flow).
        // 3. Else, fail or try global email search (ambiguous).

        if (isValidUUID(username)) {
            return loadByUserId(UUID.fromString(username));
        }

        if (username.contains("|")) {
            String[] parts = username.split("\\|", 2);
            String email = parts[0];
            String companyIdentifier = parts[1];
            return loadByEmailAndCompany(email, companyIdentifier);
        }

        throw new UsernameNotFoundException(
                "Invalid username format. Expected 'email|companyId' or 'email|domain' or 'UUID'");
    }

    private UserDetails loadByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
        return new CustomUserDetails(user);
    }

    private UserDetails loadByEmailAndCompany(String email, String companyIdentifier) {
        // Resolve Company
        Company company;
        if (isValidUUID(companyIdentifier)) {
            UUID companyId = UUID.fromString(companyIdentifier);
            company = companyRepository.findById(companyId)
                    .orElseThrow(
                            () -> new UsernameNotFoundException("Company not found with ID: " + companyIdentifier));
        } else {
            company = companyRepository.findByDomain(companyIdentifier)
                    .orElseThrow(
                            () -> new UsernameNotFoundException("Company not found with domain: " + companyIdentifier));
        }

        User user = userRepository.findByEmailAndCompanyId(email, company.getId())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found: " + email + " in company: " + company.getName()));

        return new CustomUserDetails(user);
    }

    private boolean isValidUUID(String str) {
        try {
            UUID.fromString(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
