package com.medilabosolutions.report_service.service;

import com.medilabosolutions.report_service.client.NoteServiceClient;
import com.medilabosolutions.report_service.client.PatientServiceClient;
import com.medilabosolutions.report_service.dto.NoteDTO;
import com.medilabosolutions.report_service.dto.PatientDTO;
import com.medilabosolutions.report_service.dto.ReportDTO;
import com.medilabosolutions.report_service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private PatientServiceClient patientServiceClient;

    @Mock
    private NoteServiceClient noteServiceClient;

    @InjectMocks
    private ReportService reportService;

    private PatientDTO patient;
    private List<NoteDTO> notes;

    @BeforeEach
    void setUp() {
        // Patient de test - correspond à votre record PatientDTO
        patient = new PatientDTO(
                1L,
                "John",
                "Doe",
                LocalDate.of(1980, 5, 15),
                "M",
                "123 Main St",
                "555-1234"
        );

        // Notes de test - correspondent à votre record NoteDTO
        notes = List.of(
                new NoteDTO("1", 1L, "John Doe", "Le patient présente des vertiges et un cholestérol élevé"),
                new NoteDTO("2", 1L, "John Doe", "Taille: 180cm, Poids: 85kg, Fumeur"),
                new NoteDTO("3", 1L, "John Doe", "Examen: hémoglobine A1C anormale")
        );
    }

    @Test
    void generateReport_ShouldReturnCorrectRiskAssessment() {
        // Given
        when(patientServiceClient.getPatientById(anyLong()))
                .thenReturn(new ResponseEntity<>(patient, HttpStatus.OK));
        when(noteServiceClient.getNotesByPatientId(anyLong()))
                .thenReturn(new ResponseEntity<>(notes, HttpStatus.OK));

        // When
        ReportDTO result = reportService.generateReport(1L);

        // Then - Vérifie la structure de ReportDTO
        assertThat(result).isNotNull();
        assertThat(result.patientId()).isEqualTo(1L);
        assertThat(result.fullName()).isEqualTo("John Doe"); // firstName + lastName
        assertThat(result.age()).isEqualTo(Period.between(patient.birthdate(), LocalDate.now()).getYears());
        assertThat(result.gender()).isEqualTo("M");
        assertThat(result.riskLevel()).isIn("None", "Borderline", "In Danger", "Early onset");

        verify(patientServiceClient).getPatientById(1L);
        verify(noteServiceClient).getNotesByPatientId(1L);
    }

    @Test
    void generateReport_WhenNoTriggers_ShouldReturnNoneRisk() {
        // Given
        List<NoteDTO> notesWithoutTriggers = List.of(
                new NoteDTO("1", 1L, "John Doe", "Le patient se porte bien"),
                new NoteDTO("2", 1L, "John Doe", "Examen de routine normal")
        );

        when(patientServiceClient.getPatientById(anyLong()))
                .thenReturn(new ResponseEntity<>(patient, HttpStatus.OK));
        when(noteServiceClient.getNotesByPatientId(anyLong()))
                .thenReturn(new ResponseEntity<>(notesWithoutTriggers, HttpStatus.OK));

        // When
        ReportDTO result = reportService.generateReport(1L);

        // Then
        assertThat(result.riskLevel()).isEqualTo("None");
    }

    @Test
    void generateReport_WhenBorderlineRisk_ShouldReturnBorderline() {
        // Given - Patient >30 ans avec 2-5 déclencheurs
        PatientDTO olderPatient = new PatientDTO(
                1L, "John", "Doe", LocalDate.of(1960, 5, 15), "M", "123 Main St", "555-1234"
        );

        List<NoteDTO> borderlineNotes = List.of(
                new NoteDTO("1", 1L, "John Doe", "Vertiges et cholestérol"), // 2 déclencheurs
                new NoteDTO("2", 1L, "John Doe", "Taille et poids") // 2 déclencheurs
        );

        when(patientServiceClient.getPatientById(anyLong()))
                .thenReturn(new ResponseEntity<>(olderPatient, HttpStatus.OK));
        when(noteServiceClient.getNotesByPatientId(anyLong()))
                .thenReturn(new ResponseEntity<>(borderlineNotes, HttpStatus.OK));

        // When
        ReportDTO result = reportService.generateReport(1L);

        // Then
        assertThat(result.riskLevel()).isEqualTo("Borderline");
    }

    @Test
    void generateReport_WhenInDangerRiskForMaleUnder30_ShouldReturnInDanger() {
        // Given - Homme <30 ans avec 3 déclencheurs
        PatientDTO youngMalePatient = new PatientDTO(
                1L, "John", "Doe", LocalDate.now().minusYears(25), "M", "123 Main St", "555-1234"
        );

        List<NoteDTO> dangerNotes = List.of(
                new NoteDTO("1", 1L, "John Doe", "Vertiges, cholestérol, fumeur") // 3 déclencheurs
        );

        when(patientServiceClient.getPatientById(anyLong()))
                .thenReturn(new ResponseEntity<>(youngMalePatient, HttpStatus.OK));
        when(noteServiceClient.getNotesByPatientId(anyLong()))
                .thenReturn(new ResponseEntity<>(dangerNotes, HttpStatus.OK));

        // When
        ReportDTO result = reportService.generateReport(1L);

        // Then
        assertThat(result.riskLevel()).isEqualTo("In Danger");
    }

    @Test
    void generateReport_WhenEarlyOnsetRiskForFemaleUnder30_ShouldReturnEarlyOnset() {
        // Given - Femme <30 ans avec 7 déclencheurs
        PatientDTO youngFemalePatient = new PatientDTO(
                1L, "Jane", "Smith", LocalDate.now().minusYears(25), "F", "123 Main St", "555-1234"
        );

        List<NoteDTO> earlyOnsetNotes = List.of(
                new NoteDTO("1", 1L, "Jane Smith", "hémoglobine a1c microalbumine taille poids fumeuse anormal cholestérol") // 7 déclencheurs
        );

        when(patientServiceClient.getPatientById(anyLong()))
                .thenReturn(new ResponseEntity<>(youngFemalePatient, HttpStatus.OK));
        when(noteServiceClient.getNotesByPatientId(anyLong()))
                .thenReturn(new ResponseEntity<>(earlyOnsetNotes, HttpStatus.OK));

        // When
        ReportDTO result = reportService.generateReport(1L);

        // Then
        assertThat(result.riskLevel()).isEqualTo("Early onset");
    }

    @Test
    void generateReport_WhenPatientServiceFails_ShouldThrowException() {
        // Given
        when(patientServiceClient.getPatientById(anyLong()))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        // When & Then
        assertThatThrownBy(() -> reportService.generateReport(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Erreur lors de la récupération du patient");

        verify(noteServiceClient, never()).getNotesByPatientId(anyLong());
    }

    @Test
    void generateReport_WhenNoteServiceFails_ShouldThrowException() {
        // Given
        when(patientServiceClient.getPatientById(anyLong()))
                .thenReturn(new ResponseEntity<>(patient, HttpStatus.OK));
        when(noteServiceClient.getNotesByPatientId(anyLong()))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        // When & Then
        assertThatThrownBy(() -> reportService.generateReport(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Erreur lors de la récupération des notes");

        verify(patientServiceClient).getPatientById(1L);
        verify(noteServiceClient).getNotesByPatientId(1L);
    }

    @Test
    void generateReport_ShouldHandleCaseInsensitiveTriggers() {
        // Given - Triggers en majuscules et minuscules mélangés
        List<NoteDTO> mixedCaseNotes = List.of(
                new NoteDTO("1", 1L, "John Doe", "HÉMOGLOBINE A1C et CHOLESTÉROL"),
                new NoteDTO("2", 1L, "John Doe", "FUMEUR et ANORMAL")
        );

        when(patientServiceClient.getPatientById(anyLong()))
                .thenReturn(new ResponseEntity<>(patient, HttpStatus.OK));
        when(noteServiceClient.getNotesByPatientId(anyLong()))
                .thenReturn(new ResponseEntity<>(mixedCaseNotes, HttpStatus.OK));

        // When
        ReportDTO result = reportService.generateReport(1L);

        // Then - Doit détecter les 4 triggers malgré la casse
        assertThat(result.riskLevel()).isEqualTo("Borderline");
    }

    @Test
    void generateReport_ShouldCountUniqueTriggersNotOccurrences() {
        // Given - Même trigger répété plusieurs fois
        List<NoteDTO> repeatedTriggerNotes = List.of(
                new NoteDTO("1", 1L, "John Doe", "cholestérol cholestérol cholestérol") // 3 fois le même trigger
        );

        when(patientServiceClient.getPatientById(anyLong()))
                .thenReturn(new ResponseEntity<>(patient, HttpStatus.OK));
        when(noteServiceClient.getNotesByPatientId(anyLong()))
                .thenReturn(new ResponseEntity<>(repeatedTriggerNotes, HttpStatus.OK));

        // When
        ReportDTO result = reportService.generateReport(1L);

        // Then - Doit compter 1 trigger unique (pas 3)
        assertThat(result.riskLevel()).isEqualTo("None"); // Seulement 1 trigger unique
    }

    @Test
    void generateReport_ShouldConcatenateFirstNameAndLastName() {
        // Given
        when(patientServiceClient.getPatientById(anyLong()))
                .thenReturn(new ResponseEntity<>(patient, HttpStatus.OK));
        when(noteServiceClient.getNotesByPatientId(anyLong()))
                .thenReturn(new ResponseEntity<>(notes, HttpStatus.OK));

        // When
        ReportDTO result = reportService.generateReport(1L);

        // Then - Vérifie que le nom complet est correctement formaté
        assertThat(result.fullName()).isEqualTo("John Doe");
        assertThat(result.fullName()).isEqualTo(patient.firstName() + " " + patient.lastName());
    }

    @Test
    void generateReport_ShouldCalculateCorrectAge() {
        // Given
        LocalDate birthdate = LocalDate.of(1990, 6, 1);
        PatientDTO testPatient = new PatientDTO(1L, "Test", "User", birthdate, "M", "Address", "Phone");

        when(patientServiceClient.getPatientById(anyLong()))
                .thenReturn(new ResponseEntity<>(testPatient, HttpStatus.OK));
        when(noteServiceClient.getNotesByPatientId(anyLong()))
                .thenReturn(new ResponseEntity<>(notes, HttpStatus.OK));

        // When
        ReportDTO result = reportService.generateReport(1L);

        // Then - Vérifie que l'âge est correctement calculé
        int expectedAge = Period.between(birthdate, LocalDate.now()).getYears();
        assertThat(result.age()).isEqualTo(expectedAge);
    }

    @Test
    void getNotesByPatientId_WhenSuccessful_ShouldReturnNotes() {
        // Given
        when(noteServiceClient.getNotesByPatientId(1L))
                .thenReturn(new ResponseEntity<>(notes, HttpStatus.OK));

        // When
        List<NoteDTO> result = reportService.getNotesByPatientId(1L);

        // Then
        assertThat(result).hasSize(3);
        assertThat(result.getFirst().patId()).isEqualTo(1L);
        assertThat(result.getFirst().patient()).isEqualTo("John Doe");
        verify(noteServiceClient).getNotesByPatientId(1L);
    }

    @Test
    void getPatientById_WhenSuccessful_ShouldReturnPatient() {
        // Given
        when(patientServiceClient.getPatientById(1L))
                .thenReturn(new ResponseEntity<>(patient, HttpStatus.OK));

        // When
        PatientDTO result = reportService.getPatientById(1L);

        // Then
        assertThat(result).isEqualTo(patient);
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.firstName()).isEqualTo("John");
        assertThat(result.lastName()).isEqualTo("Doe");
        verify(patientServiceClient).getPatientById(1L);
    }

    @Test
    void generateReport_WithDifferentGendersAndAges_ShouldReturnCorrectRiskLevels() {
        // Test homme <30 ans avec 3 triggers -> In Danger
        PatientDTO youngMale = new PatientDTO(1L, "John", "Doe", LocalDate.now().minusYears(25), "M", "Addr", "Phone");
        when(patientServiceClient.getPatientById(anyLong()))
                .thenReturn(new ResponseEntity<>(youngMale, HttpStatus.OK));
        when(noteServiceClient.getNotesByPatientId(anyLong()))
                .thenReturn(new ResponseEntity<>(List.of(
                        new NoteDTO("1", 1L, "John Doe", "vertiges cholestérol fumeur")
                ), HttpStatus.OK));

        ReportDTO youngMaleResult = reportService.generateReport(1L);
        assertThat(youngMaleResult.riskLevel()).isEqualTo("In Danger");

        // Test femme <30 ans avec 4 triggers -> In Danger
        PatientDTO youngFemale = new PatientDTO(2L, "Jane", "Smith", LocalDate.now().minusYears(25), "F", "Addr", "Phone");
        when(patientServiceClient.getPatientById(anyLong()))
                .thenReturn(new ResponseEntity<>(youngFemale, HttpStatus.OK));
        when(noteServiceClient.getNotesByPatientId(anyLong()))
                .thenReturn(new ResponseEntity<>(List.of(
                        new NoteDTO("1", 2L, "Jane Smith", "vertiges cholestérol fumeur anormal")
                ), HttpStatus.OK));

        ReportDTO youngFemaleResult = reportService.generateReport(2L);
        assertThat(youngFemaleResult.riskLevel()).isEqualTo("In Danger");
    }
}