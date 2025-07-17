package com.medilabosolutions.frontend_service.dto;

public record NoteDTO(
        String id,
        int patId,
        String patient,
        String note
) {
}
