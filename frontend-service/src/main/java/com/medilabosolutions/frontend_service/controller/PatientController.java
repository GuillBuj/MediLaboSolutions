package com.medilabosolutions.frontend_service.controller;

import com.medilabosolutions.frontend_service.dto.*;
import com.medilabosolutions.frontend_service.proxy.GatewayProxy;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping()
@RequiredArgsConstructor
@Slf4j
public class PatientController {

    private final GatewayProxy gatewayProxy;

    @GetMapping("/patient/{id}")
    public String getPatientReportAndNotes(@PathVariable int id, Model model) {
        log.info("getPatientReportAndNotes called");

        PatientDTO patient = gatewayProxy.getPatient(id);
        log.info("Patient {} retrieved: {}", id, patient);

        ReportDTO report = gatewayProxy.getPatientReport(id);
        log.info("Patient {} report: {}", id, report);

        List<NoteDTO> notes = gatewayProxy.getPatientNotes(id);
        log.info("Patient {} notes: {}", id, notes);

        model.addAttribute("patient", patient);
        model.addAttribute("notes", notes);
        model.addAttribute("report", report);
        model.addAttribute("newNote", new NoteCreateDTO(id, report.fullName(), ""));

        return "patient-report-and-notes";
    }

    @GetMapping("/patients/create")
    public String showCreatePatientForm(Model model) {
        log.info("showCreatePatientForm called");

        model.addAttribute("patient", new PatientCreateDTO());
        return "patient-form";
    }

    @PostMapping("/patients/create")
    public String createPatient(@Valid PatientCreateDTO patientCreateDTO,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        log.info("createPatient called for: {}", patientCreateDTO);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la création du patient");
            return "patient-form";
        }

        int id = gatewayProxy.createPatient(patientCreateDTO).id().intValue();
        log.info("Patient {} created: {}", id, patientCreateDTO);

        return "redirect:/patient/" + id;
    }

    @PostMapping("/patient/{id}/add-note")
    public String addNote(@PathVariable int id, @Valid NoteCreateDTO noteCreateDTO){
        log.info("addNote called for {}:", noteCreateDTO);

        gatewayProxy.createNote(noteCreateDTO);

        return "redirect:/patient/" + id;
    }

    @PostMapping("/patient/{id}/update")
    public String updatePatient(@PathVariable int id,
                                @Valid PatientUpdateDTO patientUpdateDTO,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        log.info("updatePatient called for: {}", patientUpdateDTO);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la création du patient");
            return "patient-form";
        }

        gatewayProxy.updatePatient(id, patientUpdateDTO);

        return "redirect:/patient/" + id;
    }

    @PostMapping("/patient/{id}/delete")
    public String deletePatient(@PathVariable int id) {
        log.info("deletePatient called");

        gatewayProxy.deletePatient(id);

        return "redirect:/home";
    }
}
