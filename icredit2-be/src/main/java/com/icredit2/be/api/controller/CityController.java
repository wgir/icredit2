package com.icredit2.be.api.controller;

import com.icredit2.be.api.dto.CityDtos;
import com.icredit2.be.service.CityService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/cities") // Using /v1 to match other controllers if any
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @PostMapping
    public ResponseEntity<CityDtos.CityResponse> create(@Valid @RequestBody CityDtos.CityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cityService.create(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('read_city')")
    public List<CityDtos.CityResponse> list() {
        return cityService.findAll();
    }

    @GetMapping("/{id}")
    public CityDtos.CityResponse get(@PathVariable UUID id) {
        return cityService.findById(id);
    }

    @PutMapping("/{id}")
    public CityDtos.CityResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody CityDtos.CityRequest request) {
        return cityService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        cityService.delete(id);
    }
}
