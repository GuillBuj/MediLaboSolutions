package com.medilabosolutions.note_service.mapper;

import com.medilabosolutions.note_service.dto.NoteCreateDTO;
import com.medilabosolutions.note_service.model.Note;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NoteMapper {
    public Note toEntity(NoteCreateDTO dto) {
        if (dto == null) return null;

        Note note = new Note();
        note.setPatientId(dto.patientId());
        note.setContent(dto.content());

        return note;
    }

    public NoteCreateDTO toDto(Note entity) {
        if (entity == null) return null;
        return new NoteCreateDTO(entity.getPatientId(),entity.getContent());
    }
}
