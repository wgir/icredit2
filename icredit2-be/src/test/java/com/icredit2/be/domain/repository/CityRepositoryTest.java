package com.icredit2.be.domain.repository;

import com.icredit2.be.domain.model.City;
import com.icredit2.be.domain.model.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CityRepositoryTest {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Company testCompany;
    private City testCity;

    @BeforeEach
    void setUp() {
        testCompany = Company.builder()
                .name("Test Company")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        entityManager.persist(testCompany);

        testCity = City.builder()
                .name("Test City")
                .company(testCompany)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        entityManager.persist(testCity);
        entityManager.flush();
    }

    // --- Save Operations ---

    @Test
    void shouldSaveCityWhenValid() {
        // Arrange
        City city = City.builder()
                .name("New City")
                .company(testCompany)
                .build();

        // Act
        City savedCity = cityRepository.save(city);

        // Assert
        assertThat(savedCity).isNotNull();
        assertThat(savedCity.getId()).isNotNull();
        assertThat(savedCity.getName()).isEqualTo("New City");
        assertThat(savedCity.getCompany()).isEqualTo(testCompany);
    }

    // --- Find Operations ---

    @Test
    void shouldFindAllCitiesByCompanyIdWhenExist() {
        // Arrange
        City anotherCity = City.builder()
                .name("Another City")
                .company(testCompany)
                .build();
        entityManager.persist(anotherCity);
        entityManager.flush();

        // Act
        List<City> cities = cityRepository.findByCompanyId(testCompany.getId());

        // Assert
        assertThat(cities).hasSize(2);
        assertThat(cities).extracting(City::getName).containsExactlyInAnyOrder("Test City", "Another City");
    }

    @Test
    void shouldReturnEmptyListWhenCompanyHasNoCities() {
        // Arrange
        Company otherCompany = Company.builder()
                .name("Other Company")
                .build();
        entityManager.persist(otherCompany);
        entityManager.flush();

        // Act
        List<City> cities = cityRepository.findByCompanyId(otherCompany.getId());

        // Assert
        assertThat(cities).isEmpty();
    }

    @Test
    void shouldFindCityByIdAndCompanyIdWhenExists() {
        // Act
        Optional<City> foundCity = cityRepository.findByIdAndCompanyId(testCity.getId(), testCompany.getId());

        // Assert
        assertThat(foundCity).isPresent();
        assertThat(foundCity.get().getName()).isEqualTo("Test City");
    }

    @Test
    void shouldReturnEmptyWhenCityIdDoesNotMatchCompanyId() {
        // Arrange
        Company otherCompany = Company.builder()
                .name("Other Company")
                .build();
        entityManager.persist(otherCompany);
        entityManager.flush();

        // Act
        Optional<City> foundCity = cityRepository.findByIdAndCompanyId(testCity.getId(), otherCompany.getId());

        // Assert
        assertThat(foundCity).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenCityIdDoesNotExist() {
        // Act
        Optional<City> foundCity = cityRepository.findByIdAndCompanyId(UUID.randomUUID(), testCompany.getId());

        // Assert
        assertThat(foundCity).isEmpty();
    }

    // --- Custom Query Operations ---

    @Test
    void shouldReturnTrueWhenCityExistsByNameAndCompanyId() {
        // Act
        boolean exists = cityRepository.existsByNameAndCompanyId("Test City", testCompany.getId());

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenCityDoesNotExistByNameAndCompanyId() {
        // Act
        boolean exists = cityRepository.existsByNameAndCompanyId("Non Existent City", testCompany.getId());

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    void shouldReturnFalseWhenCityExistsButDifferentCompany() {
        // Arrange
        Company otherCompany = Company.builder()
                .name("Other Company")
                .build();
        entityManager.persist(otherCompany);

        City otherCity = City.builder()
                .name("Test City") // Same name as testCity
                .company(otherCompany)
                .build();
        entityManager.persist(otherCity);
        entityManager.flush();

        // Act
        boolean exists = cityRepository.existsByNameAndCompanyId("Test City", otherCompany.getId()); // Checking in
                                                                                                     // other company
        boolean existsInOriginal = cityRepository.existsByNameAndCompanyId("Test City", testCompany.getId());

        // Assert
        assertThat(exists).isTrue();
        assertThat(existsInOriginal).isTrue();

        // Edge case: check if name exists in a company that doesn't have it
        Company emptyCompany = Company.builder().name("Empty Company").build();
        entityManager.persist(emptyCompany);
        entityManager.flush();

        boolean existsInEmpty = cityRepository.existsByNameAndCompanyId("Test City", emptyCompany.getId());
        assertThat(existsInEmpty).isFalse();
    }

    // --- Update Operations ---

    @Test
    void shouldUpdateCityName() {
        // Arrange
        City existingCity = entityManager.find(City.class, testCity.getId());
        existingCity.setName("Updated City");

        // Act
        City updatedCity = cityRepository.save(existingCity);

        // Assert
        assertThat(updatedCity.getName()).isEqualTo("Updated City");

        City fromDb = entityManager.find(City.class, testCity.getId());
        assertThat(fromDb.getName()).isEqualTo("Updated City");
    }

    // --- Delete Operations ---

    @Test
    void shouldDeleteCity() {
        // Act
        cityRepository.delete(testCity);

        // Assert
        City deletedCity = entityManager.find(City.class, testCity.getId());
        assertThat(deletedCity).isNull();
    }

    @Test
    void shouldDeleteById() {
        // Act
        cityRepository.deleteById(testCity.getId());

        // Assert
        City deletedCity = entityManager.find(City.class, testCity.getId());
        assertThat(deletedCity).isNull();
    }
}
