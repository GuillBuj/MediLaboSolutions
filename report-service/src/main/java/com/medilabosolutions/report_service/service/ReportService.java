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

@Service
@AllArgsConstructor
@Slf4j
public class ReportService {

    private final NoteServiceClient noteServiceClient;
    private final PatientServiceClient patientServiceClient;

    @PostConstruct
    public void init() {
        Long testPatientId = 1L;  // Assurez-vous que cet ID existe dans les services

        // Test de l'appel au service Note
        List<NoteDTO> notes = getNotesByPatientId(testPatientId);
        log.info("Notes for patient {}: {}", testPatientId, notes);

        // Test de l'appel au service Patient
        PatientDTO patient = getPatientById(testPatientId);
        log.info("Patient details for ID {} : {}", testPatientId, patient);
    }

    public ReportDTO generateReport(Long patientId) {
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

    private String evaluateRisk(int age, String gender, int triggerCount) {
        if (triggerCount == 0) return "None";

        if (triggerCount >= 2 && triggerCount <= 5 && age > 30)
            return "Borderline";

        if (age < 30) {
            if (gender.equalsIgnoreCase("M")) {
                if (triggerCount >= 5) return "Early onset";
                if (triggerCount >= 3) return "In Danger";
            } else {
                if (triggerCount >= 7) return "Early onset";
                if (triggerCount >= 4) return "In Danger";
            }
        } else {
            if (triggerCount >= 8) return "Early onset";
            if (triggerCount >= 6) return "In Danger";
        }

        return "None";
    }

    private List<NoteDTO> getNotesByPatientId(Long patientId) {
        log.info("----- Getting notes for patient {}", patientId);
        ResponseEntity<List<NoteDTO>> response = noteServiceClient.getNotesByPatientId(patientId);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new ResourceNotFoundException("Erreur lors de la récupération des notes");
        }
    }

    private PatientDTO getPatientById(Long patientId) {
        log.info("----- Getting patient {}", patientId);
        ResponseEntity<PatientDTO> response = patientServiceClient.getPatientById(patientId);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new ResourceNotFoundException("Erreur lors de la récupération du patient");
        }
    }
}
