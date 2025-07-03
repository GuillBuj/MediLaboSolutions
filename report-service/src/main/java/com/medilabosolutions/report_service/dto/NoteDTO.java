package com.medilabosolutions.report_service.dto;


public record NoteDTO(
        String id,
        Long patId,
        String patient,
        String note
) {
}
