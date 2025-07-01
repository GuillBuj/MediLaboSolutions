package com.medilabosolutions.patient_service.dto;

import com.medilabosolutions.patient_service.enums.Gender;

import java.time.LocalDate;

public record PatientListItemDTO(
        String firstName,
        String lastName,
        LocalDate birthdate,
        Gender gender,
        String address,
        String phone
) {
}
