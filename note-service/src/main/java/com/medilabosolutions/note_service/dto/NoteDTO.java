package com.medilabosolutions.note_service.dto;

public record NoteDTO(
        String id,
        int patId,
        String patient,
        String note
) {
}
