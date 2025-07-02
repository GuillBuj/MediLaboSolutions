package com.medilabosolutions.patient_service.exception;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(Long id) {
        super("Patient non trouvé avec l'ID : " + id);
    }
}
