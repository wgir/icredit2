package com.icredit2.be.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icredit2.be.api.dto.CityDtos;
import com.icredit2.be.api.exception.ResourceConflictException;
import com.icredit2.be.api.exception.ResourceNotFoundException;
import com.icredit2.be.service.CityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CityController.class)
@AutoConfigureMockMvc(addFilters = false)
class CityControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private CityService cityService;

        @MockitoBean
        private com.icredit2.be.security.JwtService jwtService;

        @MockitoBean
        private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

        // --- Create Tests ---

        @Test
        void shouldCreateCityWhenRequestIsValid() throws Exception {
                // Arrange
                CityDtos.CityRequest request = new CityDtos.CityRequest("New City");
                CityDtos.CityResponse response = new CityDtos.CityResponse(UUID.randomUUID(), "New City");

                when(cityService.create(any(CityDtos.CityRequest.class))).thenReturn(response);

                // Act & Assert
                mockMvc.perform(post("/v1/cities")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(response.id().toString()))
                                .andExpect(jsonPath("$.name").value(response.name()));
        }

        @Test
        void shouldReturnBadRequestWhenCreateRequestIsInvalid() throws Exception {
                // Arrange
                CityDtos.CityRequest request = new CityDtos.CityRequest(""); // Empty name

                // Act & Assert
                mockMvc.perform(post("/v1/cities")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturnConflictWhenCreateCityAlreadyExists() throws Exception {
                // Arrange
                CityDtos.CityRequest request = new CityDtos.CityRequest("Existing City");

                when(cityService.create(any(CityDtos.CityRequest.class)))
                                .thenThrow(new ResourceConflictException("City name already exists"));

                // Act & Assert
                mockMvc.perform(post("/v1/cities")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isConflict());
        }

        // --- List Tests ---

        @Test
        void shouldReturnListOfCitiesWhenCitiesExist() throws Exception {
                // Arrange
                CityDtos.CityResponse city1 = new CityDtos.CityResponse(UUID.randomUUID(), "City 1");
                CityDtos.CityResponse city2 = new CityDtos.CityResponse(UUID.randomUUID(), "City 2");
                List<CityDtos.CityResponse> cities = List.of(city1, city2);

                when(cityService.findAll()).thenReturn(cities);

                // Act & Assert
                mockMvc.perform(get("/v1/cities"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].name").value("City 1"))
                                .andExpect(jsonPath("$[1].name").value("City 2"));
        }

        // --- Get By ID Tests ---

        @Test
        void shouldReturnCityWhenIdExists() throws Exception {
                // Arrange
                UUID cityId = UUID.randomUUID();
                CityDtos.CityResponse response = new CityDtos.CityResponse(cityId, "My City");

                when(cityService.findById(cityId)).thenReturn(response);

                // Act & Assert
                mockMvc.perform(get("/v1/cities/{id}", cityId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(cityId.toString()))
                                .andExpect(jsonPath("$.name").value("My City"));
        }

        @Test
        void shouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
                // Arrange
                UUID cityId = UUID.randomUUID();
                when(cityService.findById(cityId))
                                .thenThrow(new ResourceNotFoundException("City not found"));

                // Act & Assert
                mockMvc.perform(get("/v1/cities/{id}", cityId))
                                .andExpect(status().isNotFound());
        }

        @Test
        void shouldReturnBadRequestWhenIdIsInvalid() throws Exception {
                // Act & Assert
                mockMvc.perform(get("/v1/cities/{id}", "invalid-uuid"))
                                .andExpect(status().isBadRequest());
        }

        // --- Update Tests ---

        @Test
        void shouldUpdateCityWhenRequestIsValid() throws Exception {
                // Arrange
                UUID cityId = UUID.randomUUID();
                CityDtos.CityRequest request = new CityDtos.CityRequest("Updated City");
                CityDtos.CityResponse response = new CityDtos.CityResponse(cityId, "Updated City");

                when(cityService.update(eq(cityId), any(CityDtos.CityRequest.class))).thenReturn(response);

                // Act & Assert
                mockMvc.perform(put("/v1/cities/{id}", cityId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Updated City"));
        }

        @Test
        void shouldReturnNotFoundWhenUpdateNonExistentCity() throws Exception {
                // Arrange
                UUID cityId = UUID.randomUUID();
                CityDtos.CityRequest request = new CityDtos.CityRequest("Updated City");

                when(cityService.update(eq(cityId), any(CityDtos.CityRequest.class)))
                                .thenThrow(new ResourceNotFoundException("City not found"));

                // Act & Assert
                mockMvc.perform(put("/v1/cities/{id}", cityId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isNotFound());
        }

        // --- Delete Tests ---

        @Test
        void shouldDeleteCityWhenIdExists() throws Exception {
                // Arrange
                UUID cityId = UUID.randomUUID();

                // Act & Assert
                mockMvc.perform(delete("/v1/cities/{id}", cityId))
                                .andExpect(status().isNoContent());
        }

        @Test
        void shouldReturnNotFoundWhenDeleteNonExistentCity() throws Exception {
                // Arrange
                UUID cityId = UUID.randomUUID();
                doThrow(new ResourceNotFoundException("City not found"))
                                .when(cityService).delete(cityId);

                // Act & Assert
                mockMvc.perform(delete("/v1/cities/{id}", cityId))
                                .andExpect(status().isNotFound());
        }
}
