package com.medilabosolutions.report_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabosolutions.report_service.client.NoteServiceClient;
import com.medilabosolutions.report_service.client.PatientServiceClient;
import com.medilabosolutions.report_service.dto.NoteDTO;
import com.medilabosolutions.report_service.dto.PatientDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReportControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PatientServiceClient patientServiceClient;

    @MockBean
    private NoteServiceClient noteServiceClient;

    @Test
    void report_None() throws Exception {
        PatientDTO patient = new PatientDTO(
                1L, "Test", "TestNone", LocalDate.of(1966, 12, 31),
                "F", "1 Brookside St", "100-222-3333"
        );

        when(patientServiceClient.getPatientById(1L)).thenReturn(ResponseEntity.ok(patient));
        when(noteServiceClient.getNotesByPatientId(1L)).thenReturn(ResponseEntity.ok(List.of()));

        mockMvc.perform(get("/api/report/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riskLevel").value("None"));
    }

    @Test
    void report_Borderline() throws Exception {
        PatientDTO patient = new PatientDTO(
                2L, "Test", "TestBorderline", LocalDate.of(1945, 6, 24),
                "M", "2 High St", "200-333-4444"
        );

        List<NoteDTO> notes = List.of(
                new NoteDTO("n1", 2L, "Test TestBorderline", "Patient fumeur"),
                new NoteDTO("n2", 2L, "Test TestBorderline", "Patient cholestérol")
        );

        when(patientServiceClient.getPatientById(2L)).thenReturn(ResponseEntity.ok(patient));
        when(noteServiceClient.getNotesByPatientId(2L)).thenReturn(ResponseEntity.ok(notes));

        mockMvc.perform(get("/api/report/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riskLevel").value("Borderline"));
    }

    @Test
    void report_InDanger() throws Exception {
        PatientDTO patient = new PatientDTO(
                3L, "Test", "TestInDanger", LocalDate.of(2004, 6, 18),
                "M", "3 Club Road", "300-444-5555"
        );

        List<NoteDTO> notes = List.of(
                new NoteDTO("n1", 3L, "Test TestInDanger", "Patient fumeur"),
                new NoteDTO("n2", 3L, "Test TestInDanger", "Patient cholestérol"),
                new NoteDTO("n3", 3L, "Test TestInDanger", "Patient vertiges")
        );

        when(patientServiceClient.getPatientById(3L)).thenReturn(ResponseEntity.ok(patient));
        when(noteServiceClient.getNotesByPatientId(3L)).thenReturn(ResponseEntity.ok(notes));

        mockMvc.perform(get("/api/report/{id}", 3L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riskLevel").value("In Danger"));
    }

    @Test
    void report_EarlyOnset() throws Exception {
        PatientDTO patient = new PatientDTO(
                4L, "Test", "TestEarlyOnset", LocalDate.of(2002, 6, 28),
                "F", "4 Valley Dr", "400-555-6666"
        );

        List<NoteDTO> notes = List.of(
                new NoteDTO("n1", 4L, "Test TestEarlyOnset", "Patient fumeur"),
                new NoteDTO("n2", 4L, "Test TestEarlyOnset", "Patient cholestérol"),
                new NoteDTO("n3", 4L, "Test TestEarlyOnset", "Patient vertiges"),
                new NoteDTO("n4", 4L, "Test TestEarlyOnset", "Patient poids élevé"),
                new NoteDTO("n5", 4L, "Test TestEarlyOnset", "Patient taille faible"),
                new NoteDTO("n6", 4L, "Test TestEarlyOnset", "Patient anormal"),
                new NoteDTO("n7", 4L, "Test TestEarlyOnset", "Patient microalbumine")
        );

        when(patientServiceClient.getPatientById(4L)).thenReturn(ResponseEntity.ok(patient));
        when(noteServiceClient.getNotesByPatientId(4L)).thenReturn(ResponseEntity.ok(notes));

        mockMvc.perform(get("/api/report/{id}", 4L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riskLevel").value("Early onset"));
    }
}
