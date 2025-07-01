package com.medilabosolutions.patient_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.medilabosolutions.patient_service.enums.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

public record PatientCreateDTO(
        String firstName,
        String lastName,
        LocalDate birthdate,
        Gender gender,
        String address,
        String phone
) {
}
