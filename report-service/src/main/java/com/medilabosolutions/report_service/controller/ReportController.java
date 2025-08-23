package com.medilabosolutions.report_service.controller;

import com.medilabosolutions.report_service.dto.ReportDTO;
import com.medilabosolutions.report_service.service.ReportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for handling patient risk report requests.
 * Exposes API endpoint for generating diabetes risk assessment reports.
 */
@RestController
@RequestMapping("/api/report")
@AllArgsConstructor
@Slf4j
public class ReportController {

    private final ReportService reportService;

    /**
     * Generates and retrieves a diabetes risk assessment report for a patient.
     * GET /api/report/{patientId}
     *
     * @param patientId The ID of the patient to generate the report for
     * @return ResponseEntity containing the ReportDTO with risk assessment and HTTP status 200 (OK)
     */
    @GetMapping("/{patientId}")
    public ResponseEntity<ReportDTO> getReport(@PathVariable Long patientId) {
        log.info("----- Getting report for patient {}", patientId);

        return ResponseEntity.ok(reportService.generateReport(patientId));
    }
}
