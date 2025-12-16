package com.icredit2.be.service;

import com.icredit2.be.api.dto.CityDtos;
import com.icredit2.be.api.exception.ResourceConflictException;
import com.icredit2.be.api.exception.ResourceNotFoundException;
import com.icredit2.be.domain.model.City;
import com.icredit2.be.domain.model.Company;
import com.icredit2.be.domain.repository.CityRepository;
import com.icredit2.be.domain.repository.CompanyRepository;
import com.icredit2.be.security.CompanyContext;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CityService {

    private final CityRepository cityRepository;
    private final CompanyRepository companyRepository;
    private final CompanyContext companyContext;

    public CityDtos.CityResponse create(CityDtos.CityRequest request) {
        UUID companyId = companyContext.getCompanyId();

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        if (cityRepository.existsByNameAndCompanyId(request.name().toLowerCase(), companyId)) {
            throw new ResourceConflictException("City name already exists");
        }

        City city = City.builder()
                .name(request.name())
                .company(company)
                .build();

        return map(cityRepository.save(city));
    }

    @Transactional(readOnly = true)
    public List<CityDtos.CityResponse> findAll() {
        return cityRepository.findByCompanyId(companyContext.getCompanyId())
                .stream()
                .map(this::map)
                .toList();
    }

    @Transactional(readOnly = true)
    public CityDtos.CityResponse findById(UUID cityId) {
        return cityRepository.findByIdAndCompanyId(
                cityId,
                companyContext.getCompanyId())
                .map(this::map)
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));
    }

    public CityDtos.CityResponse update(UUID cityId, CityDtos.CityRequest request) {
        City city = cityRepository.findByIdAndCompanyId(
                cityId,
                companyContext.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));

        city.setName(request.name());
        city.setActive(request.active());
        return map(city); // save is implicit in transaction, but explicit save is fine too.
    }

    public void delete(UUID cityId) {
        City city = cityRepository.findByIdAndCompanyId(
                cityId,
                companyContext.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));
        cityRepository.delete(city);
    }

    private CityDtos.CityResponse map(City city) {
        return new CityDtos.CityResponse(city.getId(), city.getName(), city.isActive(), city.getCreatedAt(),
                city.getUpdatedAt());
    }
}
