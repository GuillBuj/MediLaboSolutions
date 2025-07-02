package com.medilabosolutions.patient_service.dto;

import java.time.LocalDate;

public record PatientListItemDTO(
        String firstName,
        String lastName,
        LocalDate birthdate,
        String gender,
        String address,
        String phone
) {
}
