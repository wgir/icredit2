package com.icredit2.be.api.controller;

import com.icredit2.be.api.dto.CompanyDtos;
import com.icredit2.be.api.exception.ResourceNotFoundException;
import com.icredit2.be.service.CompanyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyController.class)
@AutoConfigureMockMvc(addFilters = false)
class CompanyControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private CompanyService companyService;

        @MockitoBean
        private com.icredit2.be.security.JwtAuthenticationFilter jwtAuthenticationFilter;

        @MockitoBean
        private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

        @Test
        @DisplayName("Should return company details when company exists")
        void shouldReturnCompanyWhenIdExists() throws Exception {
                // Arrange
                UUID companyId = UUID.randomUUID();
                LocalDateTime createdAt = LocalDateTime.now();
                CompanyDtos.CompanyResponse companyResponse = new CompanyDtos.CompanyResponse(
                                companyId,
                                "Test Company",
                                createdAt);

                given(companyService.getCompany(companyId)).willReturn(companyResponse);

                // Act & Assert
                mockMvc.perform(get("/v1/companies/{companyId}", companyId)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.id", is(companyId.toString())))
                                .andExpect(jsonPath("$.name", is("Test Company")))
                                .andExpect(jsonPath("$.created_at").exists());
        }

        @Test
        @DisplayName("Should return 404 Not Found when company does not exist")
        void shouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
                // Arrange
                UUID companyId = UUID.randomUUID();
                given(companyService.getCompany(companyId))
                                .willThrow(new ResourceNotFoundException("Company not found"));

                // Act & Assert
                mockMvc.perform(get("/v1/companies/{companyId}", companyId)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
                // Note: The response body verification depends on default Spring Boot error
                // handling or GlobalExceptionHandler.
                // Assuming standard behavior, we primarily check status.
        }

        @Test
        @DisplayName("Should return 400 Bad Request when UUID is invalid")
        void shouldReturnBadRequestWhenIdIsInvalid() throws Exception {
                // Arrange
                String invalidId = "invalid-uuid";

                // Act & Assert
                mockMvc.perform(get("/v1/companies/{companyId}", invalidId)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 500 Internal Server Error when unexpected service exception occurs")
        void shouldReturnInternalServerErrorWhenUnexpectedExceptionOccurs() throws Exception {
                // Arrange
                UUID companyId = UUID.randomUUID();
                given(companyService.getCompany(companyId))
                                .willThrow(new RuntimeException("Unexpected error"));

                // Act & Assert
                mockMvc.perform(get("/v1/companies/{companyId}", companyId)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isInternalServerError());
        }
}
