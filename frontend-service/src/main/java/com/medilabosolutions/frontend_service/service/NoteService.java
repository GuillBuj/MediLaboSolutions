package com.medilabosolutions.frontend_service.service;

import com.medilabosolutions.frontend_service.dto.NoteDTO;
import com.medilabosolutions.frontend_service.proxy.ProvNoteProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoteService {

    private final ProvNoteProxy noteProxy;

    public List<NoteDTO> getNotesForPatient(int patientId) {
        log.info("Fetching notes for patient: {}", patientId);
        try {
            List<NoteDTO> notes = noteProxy.getPatientHistory(patientId);
            log.info("Retrieved {} notes", notes.size());
            return notes;
        } catch (Exception e) {
            log.error("Error fetching notes: ", e);
            throw e;
        }
}

}
