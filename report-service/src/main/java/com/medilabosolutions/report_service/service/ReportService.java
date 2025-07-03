package com.medilabosolutions.report_service.service;

import com.medilabosolutions.report_service.client.NoteServiceClient;
import com.medilabosolutions.report_service.client.PatientServiceClient;
import com.medilabosolutions.report_service.dto.NoteDTO;
import com.medilabosolutions.report_service.dto.PatientDTO;
import com.medilabosolutions.report_service.exception.ResourceNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
