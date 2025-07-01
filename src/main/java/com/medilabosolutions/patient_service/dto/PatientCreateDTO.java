package com.medilabosolutions.patient_service.dto;

import com.medilabosolutions.patient_service.enums.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

public record PatientCreateDTO(
        String firstName,
        String lastName,
        LocalDate birthDate,
        Gender gender,
        String address,
        String phoneNumber
) {
}
