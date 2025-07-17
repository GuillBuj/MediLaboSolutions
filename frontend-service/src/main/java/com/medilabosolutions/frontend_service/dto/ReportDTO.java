package com.medilabosolutions.frontend_service.dto;

public record ReportDTO(
        Long patientId,
        String fullName,
        int age,
        String gender,
        String riskLevel
) {
}
