package com.icredit2.be.security;

import com.icredit2.be.domain.model.User;
import com.icredit2.be.domain.model.Company;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CompanyContext {

    public UUID getCompanyId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof CustomUserDetails)) {
            // Depending on requirements, we might want to return null or throw.
            // Since this is used in secured endpoints, we can assume it's safe to throw or
            // that principal is valid.
            throw new IllegalStateException("No authenticated user found with company context");
        }

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User user = userDetails.getUser();
        Company company = user.getCompany();

        if (company == null) {
            throw new IllegalStateException("Authenticated user does not belong to any company");
        }

        return company.getId();
    }
}
