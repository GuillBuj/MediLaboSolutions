package com.medilabosolutions.note_service.mapper;

import com.medilabosolutions.note_service.dto.NoteCreateDTO;
import com.medilabosolutions.note_service.dto.NoteDTO;
import com.medilabosolutions.note_service.model.Note;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NoteMapper {
    public Note toEntity(NoteCreateDTO dto) {
        if (dto == null) return null;

        Note note = new Note();
        note.setPatId(dto.patId());
        note.setPatient(dto.patient());
        note.setNote(dto.note());

        return note;
    }

    public Note toEntity(NoteDTO dto) {
        if (dto == null) return null;

        Note note = new Note();
        note.setId(dto.id());
        note.setPatId(dto.patId());
        note.setPatient(dto.patient());
        note.setNote(dto.note());

        return note;
    }

    public NoteCreateDTO toNoteCreateDTO(Note entity) {
        if (entity == null) return null;
        return new NoteCreateDTO(entity.getPatId(), entity.getPatient(), entity.getNote());
    }

    public NoteDTO toNoteListItemDTO(Note entity) {
        if (entity == null) return null;
        return new NoteDTO(entity.getId(), entity.getPatId(), entity.getPatient(), entity.getNote());
    }
}
