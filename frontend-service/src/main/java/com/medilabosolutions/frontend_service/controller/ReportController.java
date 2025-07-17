package com.medilabosolutions.frontend_service.controller;

import com.medilabosolutions.frontend_service.dto.PatientDTO;
import com.medilabosolutions.frontend_service.dto.ReportDTO;
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

    private final WebClient webClient;

    @GetMapping("/patients/{id}/report")
    public String getPatientReport(@PathVariable Long id, Model model) {
        ReportDTO report = webClient.get()
                .uri("api/report/{id}", id)
                .retrieve()
                .bodyToMono(ReportDTO.class)
                .block();
        model.addAttribute("report", report);
        return "report";
    }
}
