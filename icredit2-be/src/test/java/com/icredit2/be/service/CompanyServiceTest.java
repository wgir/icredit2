package com.icredit2.be.service;

import com.icredit2.be.api.dto.CompanyDtos;
import com.icredit2.be.api.exception.ResourceNotFoundException;
import com.icredit2.be.domain.model.Company;
import com.icredit2.be.domain.repository.CompanyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

    @Test
    @DisplayName("Should return company response when company ID exists")
    void shouldReturnCompanyResponseWhenIdExists() {
        // Arrange
        UUID companyId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Company company = Company.builder()
                .id(companyId)
                .name("Test Company")
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));

        // Act
        CompanyDtos.CompanyResponse result = companyService.getCompany(companyId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(companyId);
        assertThat(result.name()).isEqualTo("Test Company");
        assertThat(result.createdAt()).isEqualTo(now);

        verify(companyRepository).findById(companyId);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when company ID does not exist")
    void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // Arrange
        UUID companyId = UUID.randomUUID();
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> companyService.getCompany(companyId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Company not found");

        verify(companyRepository).findById(companyId);
    }
}
