package com.medilabosolutions.frontend_service.controller;

import com.medilabosolutions.frontend_service.dto.PatientDTO;
import com.medilabosolutions.frontend_service.dto.ReportDTO;
import com.medilabosolutions.frontend_service.service.PatientService;
import com.medilabosolutions.frontend_service.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("patients/{id}/report")
    public String getPatients(@PathVariable int id, Model model) {

        ReportDTO report = reportService.getPatientReport(id);

        model.addAttribute("report", report);
        return "report";
    }
}
