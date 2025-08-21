package com.medilabosolutions.frontend_service.controller;

import com.medilabosolutions.frontend_service.dto.ReportDTO;
import com.medilabosolutions.frontend_service.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ProvReportController {

    private final ReportService reportService;

    @GetMapping("patients/{id}/report")
    public String getPatients(@PathVariable int id, Model model) {

        ReportDTO report = reportService.getPatientReport(id);

        model.addAttribute("report", report);
        return "report";
    }
}
