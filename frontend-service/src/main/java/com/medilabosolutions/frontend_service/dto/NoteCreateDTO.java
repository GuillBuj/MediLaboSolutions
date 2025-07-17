package com.medilabosolutions.frontend_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NoteCreateDTO(
        @NotNull
        int patId,

        String patient,

        @NotBlank
        String note
) {
}
