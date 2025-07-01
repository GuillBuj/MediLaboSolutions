package com.medilabosolutions.patient_service.dto;

import com.medilabosolutions.patient_service.enums.Gender;

import java.time.LocalDate;

public record PatientUpdateDTO(
        long id,
        String firstName,
        String lastName,
        LocalDate birthdate,
        Gender gender,
        String address,
        String phone
) {
}
