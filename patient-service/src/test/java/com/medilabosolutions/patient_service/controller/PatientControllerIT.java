package com.medilabosolutions.patient_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabosolutions.patient_service.dto.PatientCreateDTO;
import com.medilabosolutions.patient_service.dto.PatientUpdateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "/data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PatientControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPatient_ShouldReturnCreatedPatient() throws Exception {
        // Given
        PatientCreateDTO patientCreateDTO = new PatientCreateDTO(
                "Paul",
                "Garcia",
                LocalDate.of(1992, 3, 10),
                "M",
                "321 rue de Lyon, 69001",
                "+33456789012"
        );

        // When
        ResultActions result = mockMvc.perform(post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientCreateDTO)));

        // Then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstName", is("Paul")))
                .andExpect(jsonPath("$.lastName", is("Garcia")))
                .andExpect(jsonPath("$.gender", is("M")));
    }

    @Test
    void createPatient_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given - Date de naissance dans le futur (invalide)
        PatientCreateDTO invalidPatient = new PatientCreateDTO(
                "Jean",
                "Dupont",
                LocalDate.now().plusDays(1), // Date future invalide
                "M",
                "123 rue de Paris, 75001",
                "+33123456789"
        );

        // When
        ResultActions result = mockMvc.perform(post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPatient)));

        // Then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void getAllPatients_ShouldReturnPatientsList() throws Exception {
        // When - Les données sont chargées via le script SQL
        ResultActions result = mockMvc.perform(get("/api/patients"));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].lastName", is("Dupont")))
                .andExpect(jsonPath("$[1].lastName", is("Martin")))
                .andExpect(jsonPath("$[2].lastName", is("Durand")));
    }

    @Test
    void getPatientById_ShouldReturnPatient() throws Exception {
        // When - Le patient avec ID 1 est chargé via le script SQL
        ResultActions result = mockMvc.perform(get("/api/patients/{id}", 1L));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.lastName", is("Dupont")))
                .andExpect(jsonPath("$.firstName", is("Jean")));
    }

    @Test
    void getPatientById_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        // When
        ResultActions result = mockMvc.perform(get("/api/patients/{id}", 999L));

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    void updatePatient_ShouldReturnUpdatedPatient() throws Exception {
        // Given - Le patient avec ID 1 est chargé via le script SQL
        PatientUpdateDTO updateDTO = new PatientUpdateDTO(
                1L, // ID du patient existant
                "Jean-Paul", // Prénom modifié
                "Dupont",
                LocalDate.of(1985, 5, 15),
                "M",
                "123 rue de Paris, 75001",
                "+33123456789"
        );

        // When
        ResultActions result = mockMvc.perform(put("/api/patients/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Jean-Paul")))
                .andExpect(jsonPath("$.lastName", is("Dupont")));
    }

    @Test
    void updatePatient_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        // Given
        PatientUpdateDTO updateDTO = new PatientUpdateDTO(
                999L, // ID inexistant
                "Jean",
                "Dupont",
                LocalDate.of(1985, 5, 15),
                "M",
                "123 rue de Paris, 75001",
                "+33123456789"
        );

        // When
        ResultActions result = mockMvc.perform(put("/api/patients/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)));

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    void deletePatient_ShouldReturnNoContent() throws Exception {
        // When - Supprimer le patient avec ID 1 chargé via le script SQL
        ResultActions result = mockMvc.perform(delete("/api/patients/{id}", 1L));

        // Then
        result.andExpect(status().isNoContent());

        // Verify patient is actually deleted
        mockMvc.perform(get("/api/patients/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAllPatients_ShouldRemoveAllPatients() throws Exception {
        // Verify patients exist
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        // When
        ResultActions result = mockMvc.perform(get("/api/patients/delete-all"));

        // Then
        result.andExpect(status().isOk());

        // Verify all patients are deleted
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
