package com.medilabosolutions.frontend_service.service;

import com.medilabosolutions.frontend_service.dto.NoteDTO;
import com.medilabosolutions.frontend_service.proxy.NoteProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NoteService {

    @Autowired
    private NoteProxy noteProxy;

//    public List<NoteDTO> getNotesForPatient(Long patientId) {
//        return noteProxy.getPatientHistory(patientId);
//    }
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
