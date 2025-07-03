package com.medilabosolutions.note_service.dto;

public record NoteListItemDTO(
        Long patId,
        String patient,
        String note
) {
}
