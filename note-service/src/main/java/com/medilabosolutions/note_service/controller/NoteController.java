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

@RestController
@RequestMapping("api/notes")
@AllArgsConstructor
@Slf4j
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<NoteCreateDTO> createNote(@RequestBody NoteCreateDTO noteCreateDTO) {
        log.info("Creating note : {}", noteCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.createNote(noteCreateDTO));
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<List<NoteDTO>> getAllNotes(@PathVariable(name = "id") Long id) {
        log.info("Getting all notes for: {}", id);
        return ResponseEntity.ok(noteService.getAllNotesByPatientId(id));
    }
}
