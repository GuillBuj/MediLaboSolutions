package com.medilabosolutions.note_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NoteCreateDTO(
        @NotNull
        String patientId,

        @NotBlank
        String content
) {
}
