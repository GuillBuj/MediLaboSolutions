package com.medilabosolutions.note_service.service;

import com.medilabosolutions.note_service.dto.NoteCreateDTO;
import com.medilabosolutions.note_service.dto.NoteDTO;
import com.medilabosolutions.note_service.mapper.NoteMapper;
import com.medilabosolutions.note_service.model.Note;
import com.medilabosolutions.note_service.repository.NoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing patient notes.
 * Handles business logic for note creation, retrieval, and deletion.
 */
@Service
@AllArgsConstructor
@Slf4j
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    /**
     * Creates a new note for a patient.
     * Automatically sets the current date/time for the note.
     *
     * @param noteCreateDTO DTO containing note content and patient information
     * @return NoteCreateDTO representing the created note with generated ID and timestamp
     */
    public NoteCreateDTO createNote(NoteCreateDTO noteCreateDTO) {
        log.info("Creating new note: {}", noteCreateDTO);

        Note note = noteMapper.toEntity(noteCreateDTO);
        note.setDate(LocalDateTime.now());
        Note noteCreated = noteRepository.save(note);

        log.info("Note created: {}", noteCreated);
        return noteMapper.toNoteCreateDTO(noteCreated);
    }

    /**
     * Retrieves all notes for a specific patient.
     * GET /api/notes/patient/{patientId}
     *
     * @param patientId The ID of the patient to retrieve notes for
     * @return List of NoteDTOs containing all notes for the specified patient
     */
    public List<NoteDTO> getAllNotesByPatientId(int patientId) {
        log.info("Getting all notes for patient: {}", patientId);

        return noteRepository.findByPatId(patientId)
                .stream()
                .map(noteMapper::toNoteListItemDTO)
                .collect(Collectors.toList());
    }

    public void deleteNoteById(String id) {
        log.info("Deleting note with ID: {}", id);
        noteRepository.deleteById(id);
    }

    /**
     * Deletes all notes from the system.
     * Primarily used for testing and cleanup purposes.
     * Use with caution as this operation is irreversible.
     */
    public void deleteAllNotes() {
        noteRepository.deleteAll();
    }
}
