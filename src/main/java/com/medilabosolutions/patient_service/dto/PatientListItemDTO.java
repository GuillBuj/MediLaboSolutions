package com.medilabosolutions.patient_service.dto;

import com.medilabosolutions.patient_service.enums.Gender;

import java.time.LocalDate;

public record PatientListItemDTO(
        String firstName,
        String lastName,
        LocalDate birthDate,
        Gender gender,
        String address,
        String phoneNumber
) {
}
