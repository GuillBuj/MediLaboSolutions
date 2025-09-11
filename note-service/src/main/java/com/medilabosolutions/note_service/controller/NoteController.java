package com.medilabosolutions.note_service.controller;

import com.medilabosolutions.note_service.dto.NoteCreateDTO;
import com.medilabosolutions.note_service.dto.NoteDTO;
import com.medilabosolutions.note_service.service.NoteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for handling patient notes HTTP requests.
 * Exposes API endpoints for note operations.
 */
@RestController
@RequestMapping("api/notes")
@AllArgsConstructor
@Slf4j
public class NoteController {

    private final NoteService noteService;

    /**
     * Creates a new note for a patient.
     * POST /api/notes
     *
     * @param noteCreateDTO DTO containing note content and patient information
     * @return ResponseEntity containing the created NoteCreateDTO with HTTP status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<NoteCreateDTO> createNote(@RequestBody NoteCreateDTO noteCreateDTO) {
        log.info("Creating note : {}", noteCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.createNote(noteCreateDTO));
    }

    /**
     * Retrieves all notes for a specific patient.
     * GET /api/notes/patient/{id}
     *
     * @param id The ID of the patient to retrieve notes for
     * @return ResponseEntity containing a list of NoteDTOs with HTTP status 200 (OK)
     */
    @GetMapping("/patient/{id}")
    public ResponseEntity<List<NoteDTO>> getAllNotesByPatientId(@PathVariable(name = "id", required = true) int id
    ) {
        log.info("Getting all notes for: {}", id);
        return ResponseEntity.ok(noteService.getAllNotesByPatientId(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable String id) {
        log.info("Deleting note with ID: {}", id);
        noteService.deleteNoteById(id);
    }

    /**
     * Deletes all notes from the system (primarily for testing purposes).
     * GET /api/notes/delete-all
     * Use with caution as this operation is irreversible.
     */
    @GetMapping("/delete-all")
    public void deleteAllNotes() {
        log.info("Deleting all notes");
        noteService.deleteAllNotes();
    }
}
