package com.medilabosolutions.frontend_service.controller;

import com.medilabosolutions.frontend_service.dto.PatientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PatientController {

    private final WebClient webClient;

    @GetMapping("/patients")
    public String patients(Model model) {
        List<PatientDTO> patients = webClient.get()
                .uri("api/patients")
                .retrieve()
                .bodyToFlux(PatientDTO.class)
                .collectList()
                .block();
        model.addAttribute("patients", patients);
        return "patients";
    }
}
