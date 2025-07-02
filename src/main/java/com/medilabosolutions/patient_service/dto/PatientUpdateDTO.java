package com.medilabosolutions.patient_service.dto;

import java.time.LocalDate;

public record PatientUpdateDTO(
        long id,
        String firstName,
        String lastName,
        LocalDate birthdate,
        String gender,
        String address,
        String phone
) {
}
