package com.medilabosolutions.frontend_service.controller;

import com.medilabosolutions.frontend_service.dto.PatientDTO;
import com.medilabosolutions.frontend_service.proxy.GatewayProxy;
import com.medilabosolutions.frontend_service.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final GatewayProxy gatewayProxy;

    @GetMapping
    public String home(Model model) {

        List<PatientDTO> patients = gatewayProxy.getAllPatients();

        model.addAttribute("patients", patients);
        return "patients";
    }
}
