package com.icredit2.be.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icredit2.be.api.dto.AuthDtos;
import com.icredit2.be.api.dto.CompanyDtos;
import com.icredit2.be.api.dto.UserDtos;
import com.icredit2.be.api.exception.ResourceConflictException;
import com.icredit2.be.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for this test content
class AuthControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private AuthService authService;

        @Autowired
        private ObjectMapper objectMapper;

        // We also need to mock these to satisfy the context if security was enabled,
        // but with addFilters=false, we might bypass them.
        // However, if SecurityConfig is loaded, it might look for them.
        @MockitoBean
        private com.icredit2.be.security.JwtAuthenticationFilter jwtAuthenticationFilter;
        @MockitoBean
        private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

        @Test
        @DisplayName("Should register company successfully")
        void shouldRegisterCompanySuccessfully() throws Exception {
                // Arrange
                CompanyDtos.CompanyRegistrationRequest request = new CompanyDtos.CompanyRegistrationRequest(
                                "Test Company",
                                new CompanyDtos.CompanyOwnerRequest("test@example.com", "password", "Test User"));

                UUID companyId = UUID.randomUUID();
                UUID userId = UUID.randomUUID();
                LocalDateTime now = LocalDateTime.now();

                CompanyDtos.CompanyRegistrationResponse response = new CompanyDtos.CompanyRegistrationResponse(
                                new CompanyDtos.CompanyResponse(companyId, "Test Company", now),
                                new UserDtos.UserResponse(userId, companyId, "test@example.com", "Test User", true,
                                                List.of("admin"),
                                                now),
                                new AuthDtos.AuthResponse("access-token", 3600L, "refresh-token"));

                when(authService.registerCompany(any(CompanyDtos.CompanyRegistrationRequest.class)))
                                .thenReturn(response);

                // Act & Assert
                mockMvc.perform(post("/v1/companies/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(csrf()))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.company.name", is("Test Company")))
                                .andExpect(jsonPath("$.owner.email", is("test@example.com")))
                                .andExpect(jsonPath("$.tokens.access_token", is("access-token")))
                                .andExpect(cookie().value("jwt", "access-token"))
                                .andExpect(cookie().httpOnly("jwt", true))
                                .andExpect(cookie().path("jwt", "/"));
        }

        @Test
        @DisplayName("Should login successfully")
        void shouldLoginSuccessfully() throws Exception {
                // Arrange
                AuthDtos.LoginRequest request = new AuthDtos.LoginRequest("test@example.com", "password");
                AuthDtos.AuthResponse response = new AuthDtos.AuthResponse("access-token", 3600L, "refresh-token");

                when(authService.login(any(AuthDtos.LoginRequest.class))).thenReturn(response);

                // Act & Assert
                mockMvc.perform(post("/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(csrf()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.access_token", is("access-token")))
                                .andExpect(cookie().value("jwt", "access-token"))
                                .andExpect(cookie().httpOnly("jwt", true))
                                .andExpect(cookie().path("jwt", "/"));
        }

        @Test
        @DisplayName("Should return 409 Conflict when registration fails with conflict")
        void shouldReturnConflictWhenRegistrationFails() throws Exception {
                // Arrange
                CompanyDtos.CompanyRegistrationRequest request = new CompanyDtos.CompanyRegistrationRequest(
                                "Test Company",
                                new CompanyDtos.CompanyOwnerRequest("test@example.com", "password", "Test User"));

                when(authService.registerCompany(any(CompanyDtos.CompanyRegistrationRequest.class)))
                                .thenThrow(new ResourceConflictException("Email already registered"));

                // Act & Assert
                mockMvc.perform(post("/v1/companies/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(csrf()))
                                .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("Should return 401 Unauthorized when login fails")
        void shouldReturnUnauthorizedWhenLoginFails() throws Exception {
                // Arrange
                AuthDtos.LoginRequest request = new AuthDtos.LoginRequest("test@example.com", "wrongpassword");

                when(authService.login(any(AuthDtos.LoginRequest.class)))
                                .thenThrow(new org.springframework.security.authentication.BadCredentialsException(
                                                "Bad credentials"));

                // Act & Assert
                mockMvc.perform(post("/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(csrf()))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Should return 400 Bad Request on validation error")
        void shouldReturnBadRequestOnValidationError() throws Exception {
                // Arrange - Invalid email
                AuthDtos.LoginRequest request = new AuthDtos.LoginRequest("invalid-email", "password");

                // Act & Assert
                mockMvc.perform(post("/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(csrf()))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should logout successfully")
        void shouldLogoutSuccessfully() throws Exception {
                mockMvc.perform(post("/v1/auth/logout")
                                .with(csrf()))
                                .andExpect(status().isOk())
                                .andExpect(cookie().value("jwt", ""))
                                .andExpect(cookie().maxAge("jwt", 0))
                                .andExpect(cookie().httpOnly("jwt", true))
                                .andExpect(cookie().path("jwt", "/"));
        }
}
