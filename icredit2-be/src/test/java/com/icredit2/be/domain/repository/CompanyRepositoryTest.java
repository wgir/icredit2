package com.icredit2.be.domain.repository;

import com.icredit2.be.domain.model.Company;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CompanyRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    @DisplayName("Should save a company and generate an ID")
    void shouldSaveCompanyAndGenerateId() {
        // Arrange
        Company company = Company.builder()
                .name("New Company")
                .build();

        // Act
        Company savedCompany = companyRepository.saveAndFlush(company);

        // Assert
        assertThat(savedCompany).isNotNull();
        assertThat(savedCompany.getId()).isNotNull();
        assertThat(savedCompany.getName()).isEqualTo("New Company");
        assertThat(savedCompany.getCreatedAt()).isNotNull(); // Check @CreationTimestamp
    }

    @Test
    @DisplayName("Should find company by ID when it exists")
    void shouldFindCompanyByIdWhenExists() {
        // Arrange
        Company company = Company.builder()
                .name("Existing Company")
                .build();
        Company persistedCompany = entityManager.persistAndFlush(company);
        UUID companyId = persistedCompany.getId();

        // Act
        Optional<Company> foundCompany = companyRepository.findById(companyId);

        // Assert
        assertThat(foundCompany).isPresent();
        assertThat(foundCompany.get().getName()).isEqualTo("Existing Company");
    }

    @Test
    @DisplayName("Should return empty when finding by non-existent ID")
    void shouldReturnEmptyWhenIdDoesNotExist() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();

        // Act
        Optional<Company> foundCompany = companyRepository.findById(nonExistentId);

        // Assert
        assertThat(foundCompany).isEmpty();
    }

    @Test
    @DisplayName("Should return true when company with name exists")
    void shouldReturnTrueWhenNameExists() {
        // Arrange
        Company company = Company.builder()
                .name("Unique Name")
                .build();
        entityManager.persistAndFlush(company);

        // Act
        boolean exists = companyRepository.existsByName("Unique Name");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when company with name does not exist")
    void shouldReturnFalseWhenNameDoesNotExist() {
        // Act
        boolean exists = companyRepository.existsByName("Non Existent Name");

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should update company name")
    void shouldUpdateCompanyName() {
        // Arrange
        Company company = Company.builder()
                .name("Old Name")
                .build();
        Company persistedCompany = entityManager.persistAndFlush(company);
        UUID companyId = persistedCompany.getId();

        // Act
        persistedCompany.setName("New Name");
        companyRepository.save(persistedCompany);
        entityManager.flush(); // Force sync to DB
        entityManager.clear(); // Clear cache to ensure read from DB

        Optional<Company> retrievedCompany = companyRepository.findById(companyId);

        // Assert
        assertThat(retrievedCompany).isPresent();
        assertThat(retrievedCompany.get().getName()).isEqualTo("New Name");
    }

    @Test
    @DisplayName("Should delete company")
    void shouldDeleteCompany() {
        // Arrange
        Company company = Company.builder()
                .name("To Be Deleted")
                .build();
        Company persistedCompany = entityManager.persistAndFlush(company);
        UUID companyId = persistedCompany.getId();

        // Act
        companyRepository.deleteById(companyId);
        entityManager.flush();

        Optional<Company> deletedCompany = companyRepository.findById(companyId);

        // Assert
        assertThat(deletedCompany).isEmpty();
    }
}
