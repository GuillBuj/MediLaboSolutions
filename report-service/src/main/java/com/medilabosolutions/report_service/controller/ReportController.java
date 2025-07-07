package com.medilabosolutions.report_service.controller;

import com.medilabosolutions.report_service.dto.ReportDTO;
import com.medilabosolutions.report_service.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
@AllArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/{patientId}")
    public ResponseEntity<ReportDTO> getReport(@PathVariable Long patientId) {
        System.out.println("🎯 Endpoint GET /api/report/" + patientId + " appelé !");
        return ResponseEntity.ok(
                new ReportDTO(1L,"name",30,"In Danger", "risqué")
//                reportService.generateReport(patientId
                );
    }
}
