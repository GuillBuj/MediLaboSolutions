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

@Service
@AllArgsConstructor
@Slf4j
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    public NoteCreateDTO createNote(NoteCreateDTO noteCreateDTO) {
        log.info("Creating new note: {}", noteCreateDTO);
        Note note = noteMapper.toEntity(noteCreateDTO);
        note.setDate(LocalDateTime.now());
        Note noteCreated = noteRepository.save(note);
        return noteMapper.toNoteCreateDTO(noteCreated);
    }

    public List<NoteDTO> getAllNotesByPatientId(int patientId) {
        return noteRepository.findByPatId(patientId)
                .stream()
                .map(noteMapper::toNoteListItemDTO)
                .collect(Collectors.toList());
    }

}
