package com.icredit2.be.service;

import com.icredit2.be.api.dto.CityDtos;
import com.icredit2.be.api.exception.ResourceConflictException;
import com.icredit2.be.api.exception.ResourceNotFoundException;
import com.icredit2.be.domain.model.City;
import com.icredit2.be.domain.model.Company;
import com.icredit2.be.domain.repository.CityRepository;
import com.icredit2.be.domain.repository.CompanyRepository;
import com.icredit2.be.security.CompanyContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyContext companyContext;

    @InjectMocks
    private CityService cityService;

    // --- Create Tests ---

    @Test
    void shouldCreateCityWhenRequestIsValid() {
        // Arrange
        UUID companyId = UUID.randomUUID();
        String cityName = "New City";
        CityDtos.CityRequest request = new CityDtos.CityRequest(cityName);
        Company company = new Company();
        company.setId(companyId);

        City savedCity = City.builder()
                .id(UUID.randomUUID())
                .name(cityName)
                .company(company)
                .build();

        when(companyContext.getCompanyId()).thenReturn(companyId);
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(cityRepository.existsByNameAndCompanyId(cityName.toLowerCase(), companyId)).thenReturn(false);
        when(cityRepository.save(any(City.class))).thenReturn(savedCity);

        // Act
        CityDtos.CityResponse response = cityService.create(request);

        // Assert
        assertNotNull(response);
        assertEquals(savedCity.getId(), response.id());
        assertEquals(cityName, response.name());
        verify(cityRepository, times(1)).save(any(City.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCreatingAndCompanyNotFound() {
        // Arrange
        UUID companyId = UUID.randomUUID();
        CityDtos.CityRequest request = new CityDtos.CityRequest("City");

        when(companyContext.getCompanyId()).thenReturn(companyId);
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> cityService.create(request));
        verify(cityRepository, never()).save(any(City.class));
    }

    @Test
    void shouldThrowConflictExceptionWhenCreatingAndCityNameExists() {
        // Arrange
        UUID companyId = UUID.randomUUID();
        String cityName = "Existing City";
        CityDtos.CityRequest request = new CityDtos.CityRequest(cityName);
        Company company = new Company();
        company.setId(companyId);

        when(companyContext.getCompanyId()).thenReturn(companyId);
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(cityRepository.existsByNameAndCompanyId(cityName.toLowerCase(), companyId)).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceConflictException.class, () -> cityService.create(request));
        verify(cityRepository, never()).save(any(City.class));
    }

    // --- Find All Tests ---

    @Test
    void shouldReturnListOfCitiesWhenCitiesExist() {
        // Arrange
        UUID companyId = UUID.randomUUID();
        City city1 = City.builder().id(UUID.randomUUID()).name("City 1").build();
        City city2 = City.builder().id(UUID.randomUUID()).name("City 2").build();

        when(companyContext.getCompanyId()).thenReturn(companyId);
        when(cityRepository.findByCompanyId(companyId)).thenReturn(List.of(city1, city2));

        // Act
        List<CityDtos.CityResponse> responses = cityService.findAll();

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("City 1", responses.get(0).name());
        assertEquals("City 2", responses.get(1).name());
    }

    @Test
    void shouldReturnEmptyListWhenNoCitiesExist() {
        // Arrange
        UUID companyId = UUID.randomUUID();
        when(companyContext.getCompanyId()).thenReturn(companyId);
        when(cityRepository.findByCompanyId(companyId)).thenReturn(Collections.emptyList());

        // Act
        List<CityDtos.CityResponse> responses = cityService.findAll();

        // Assert
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    // --- Find By ID Tests ---

    @Test
    void shouldReturnCityWhenIdExists() {
        // Arrange
        UUID companyId = UUID.randomUUID();
        UUID cityId = UUID.randomUUID();
        City city = City.builder().id(cityId).name("My City").build();

        when(companyContext.getCompanyId()).thenReturn(companyId);
        when(cityRepository.findByIdAndCompanyId(cityId, companyId)).thenReturn(Optional.of(city));

        // Act
        CityDtos.CityResponse response = cityService.findById(cityId);

        // Assert
        assertNotNull(response);
        assertEquals(cityId, response.id());
        assertEquals("My City", response.name());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenFindByIdAndDoesNotExist() {
        // Arrange
        UUID companyId = UUID.randomUUID();
        UUID cityId = UUID.randomUUID();

        when(companyContext.getCompanyId()).thenReturn(companyId);
        when(cityRepository.findByIdAndCompanyId(cityId, companyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> cityService.findById(cityId));
    }

    // --- Update Tests ---

    @Test
    void shouldUpdateCityWhenRequestIsValid() {
        // Arrange
        UUID companyId = UUID.randomUUID();
        UUID cityId = UUID.randomUUID();
        String newName = "Updated Name";
        CityDtos.CityRequest request = new CityDtos.CityRequest(newName);
        City existingCity = City.builder().id(cityId).name("Old Name").build();

        when(companyContext.getCompanyId()).thenReturn(companyId);
        when(cityRepository.findByIdAndCompanyId(cityId, companyId)).thenReturn(Optional.of(existingCity));

        // Act
        CityDtos.CityResponse response = cityService.update(cityId, request);

        // Assert
        assertNotNull(response);
        assertEquals(cityId, response.id());
        assertEquals(newName, response.name());
        assertEquals(newName, existingCity.getName()); // Verify object state changed
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdateAndCityDoesNotExist() {
        // Arrange
        UUID companyId = UUID.randomUUID();
        UUID cityId = UUID.randomUUID();
        CityDtos.CityRequest request = new CityDtos.CityRequest("Name");

        when(companyContext.getCompanyId()).thenReturn(companyId);
        when(cityRepository.findByIdAndCompanyId(cityId, companyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> cityService.update(cityId, request));
    }

    // --- Delete Tests ---

    @Test
    void shouldDeleteCityWhenIdExists() {
        // Arrange
        UUID companyId = UUID.randomUUID();
        UUID cityId = UUID.randomUUID();
        City city = City.builder().id(cityId).build();

        when(companyContext.getCompanyId()).thenReturn(companyId);
        when(cityRepository.findByIdAndCompanyId(cityId, companyId)).thenReturn(Optional.of(city));

        // Act
        cityService.delete(cityId);

        // Assert
        verify(cityRepository, times(1)).delete(city);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeleteAndCityDoesNotExist() {
        // Arrange
        UUID companyId = UUID.randomUUID();
        UUID cityId = UUID.randomUUID();

        when(companyContext.getCompanyId()).thenReturn(companyId);
        when(cityRepository.findByIdAndCompanyId(cityId, companyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> cityService.delete(cityId));
        verify(cityRepository, never()).delete(any());
    }
}
