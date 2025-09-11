package com.medilabosolutions.frontend_service.controller;

import com.medilabosolutions.frontend_service.dto.PatientDTO;
import com.medilabosolutions.frontend_service.proxy.GatewayProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controller handling home page requests and patient listing.
 * Maps to the "/home" endpoint.
 */
@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final GatewayProxy gatewayProxy;

    /**
     * Displays the home page with all patients.
     *
     * @param model the Spring Model object to add attributes for the view
     * @return the view name "patients" to render
     */
    @GetMapping
    public String home(Model model) {

        List<PatientDTO> patients = gatewayProxy.getAllPatients();

        model.addAttribute("patients", patients);
        return "patients";
    }
}
