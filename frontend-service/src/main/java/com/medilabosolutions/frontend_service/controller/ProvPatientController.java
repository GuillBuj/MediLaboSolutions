package com.medilabosolutions.frontend_service.controller;

import com.medilabosolutions.frontend_service.dto.PatientDTO;
import com.medilabosolutions.frontend_service.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/patients")
public class ProvPatientController {

    private final PatientService patientService;

    @GetMapping
    public String getPatients(Model model) {

        List<PatientDTO> patients = patientService.getAllPatients();

        model.addAttribute("patients", patients);
        return "patients";
    }
}
