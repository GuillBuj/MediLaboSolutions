package com.medilabosolutions.patient_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

public record PatientCreateDTO(
        String firstName,
        String lastName,
        LocalDate birthdate,
        String gender,
        String address,
        String phone
) {
}
