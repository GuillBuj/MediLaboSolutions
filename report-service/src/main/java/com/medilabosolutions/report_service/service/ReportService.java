package com.medilabosolutions.report_service.service;

import com.medilabosolutions.report_service.client.NoteServiceClient;
import com.medilabosolutions.report_service.client.PatientServiceClient;
import com.medilabosolutions.report_service.constants.TriggerConstants;
import com.medilabosolutions.report_service.dto.NoteDTO;
import com.medilabosolutions.report_service.dto.PatientDTO;
import com.medilabosolutions.report_service.dto.ReportDTO;
import com.medilabosolutions.report_service.exception.ResourceNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for generating patient risk assessment reports.
 * Analyzes patient data and notes to determine diabetes risk level.
 */
@Service
@AllArgsConstructor
@Slf4j
public class ReportService {

    private final PatientServiceClient patientServiceClient;
    private final NoteServiceClient noteServiceClient;

    /**
     * Generates a diabetes risk assessment report for a patient.
     * Combines patient information with note analysis to determine risk level.
     *
     * @param patientId The ID of the patient to generate the report for
     * @return ReportDTO containing patient information and calculated risk assessment
     * @throws ResourceNotFoundException if patient or notes cannot be retrieved
     */
    public ReportDTO generateReport(Long patientId) {
        log.info("----- Generating report for patient {}", patientId);

        PatientDTO patient = getPatientById(patientId);
        List<NoteDTO> notes = getNotesByPatientId(patientId);

        int age = Period.between(patient.birthdate(), LocalDate.now()).getYears();
        String gender = patient.gender();

        String allNotes = notes.stream()
                .map(NoteDTO::note)
                .collect(Collectors.joining(" "))
                .toLowerCase();

        int triggerCount = (int) TriggerConstants.TRIGGERS.stream()
                .map(String::toLowerCase)
                .filter(allNotes::contains)
                .count();

        String riskLevel = evaluateRisk(age, gender, triggerCount);

        return new ReportDTO(
                patient.id(),
                patient.firstName() + " " + patient.lastName(),
                age,
                gender,
                riskLevel
        );
    }

    /**
     * Evaluates diabetes risk based on age, gender, and trigger word count.
     * Implements medical risk assessment rules.
     *
     * @param age Patient's age in years
     * @param gender Patient's gender
     * @param triggerCount Number of trigger words found in patient notes
     * @return String representing the risk level ("None", "Borderline", "In Danger", "Early onset")
     */
    private String evaluateRisk(int age, String gender, int triggerCount) {
        log.info("----- Evaluating risk");

        if (triggerCount == 0) return "None";

        if (triggerCount >= 2 && triggerCount <= 5 && age > 30)
            return "Borderline";

        if (age < 30) {
            if (gender.equalsIgnoreCase("M")) {
                if (triggerCount >= 5) return "Early onset";
                if (triggerCount >= 3) return "In Danger";
            } else {
                if (triggerCount >= 7) return "Early onset";
                else if (triggerCount >= 4) return "In Danger";
            }
        } else {
            if (triggerCount >= 8) return "Early onset";
            if (triggerCount >= 6) return "In Danger";
        }

        return "None";
    }

    /**
     * Retrieves all notes for a specific patient from the notes service.
     *
     * @param patientId The ID of the patient
     * @return List of NoteDTOs for the patient
     * @throws ResourceNotFoundException if notes cannot be retrieved
     */
    protected List<NoteDTO> getNotesByPatientId(Long patientId) {
        log.info("----- Getting notes for patient {}", patientId);

        ResponseEntity<List<NoteDTO>> response = noteServiceClient.getNotesByPatientId(patientId);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new ResourceNotFoundException("Erreur lors de la récupération des notes");
        }
    }

    /**
     * Retrieves patient information from the patient service.
     *
     * @param patientId The ID of the patient
     * @return PatientDTO containing patient information
     * @throws ResourceNotFoundException if patient cannot be retrieved
     */
    protected PatientDTO getPatientById(Long patientId) {
        log.info("----- Getting patient {}", patientId);

        ResponseEntity<PatientDTO> response = patientServiceClient.getPatientById(patientId);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new ResourceNotFoundException("Erreur lors de la récupération du patient");
        }
    }
}
