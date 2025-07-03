package com.medilabosolutions.note_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NoteCreateDTO(
        @NotNull
        Long patId,

        String patient,

        @NotBlank
        String note
) {
}
