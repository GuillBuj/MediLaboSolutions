package com.medilabosolutions.patient_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public enum Gender {
    MALE("Male"),
    FEMALE("Female");

    private final String displayValue;

    // Constructeur obligatoire (même sans Lombok)
    Gender(String displayValue) {
        this.displayValue = displayValue;
    }

    // Getter obligatoire pour accéder à displayValue
    public String getDisplayValue() {
        return displayValue;
    }
}
