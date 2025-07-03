package com.medilabosolutions.note_service.dto;

public record NoteDTO(
        String id,
        Long patId,
        String patient,
        String note
) {
}
