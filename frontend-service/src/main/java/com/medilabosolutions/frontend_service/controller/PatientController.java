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

/**
 * Controller handling patient-related operations including CRUD operations and note management.
 */
@Controller
@RequestMapping()
@RequiredArgsConstructor
@Slf4j
public class PatientController {

    private final GatewayProxy gatewayProxy;

    /**
     * Retrieves and displays a patient's report and notes.
     *
     * @param id the patient ID
     * @param model the Spring Model object to add attributes for the view
     * @return the view name "patient-report-and-notes" to render
     */
    @GetMapping("/patient/{id}")
    public String getPatientReportAndNotes(@PathVariable int id, Model model) {
        log.info("getPatientReportAndNotes called");

        PatientDTO patient = gatewayProxy.getPatient(id);
        ReportDTO report = gatewayProxy.getPatientReport(id);
        List<NoteDTO> notes = gatewayProxy.getPatientNotes(id);

        model.addAttribute("patient", patient);
        model.addAttribute("notes", notes);
        model.addAttribute("report", report);
        model.addAttribute("newNote", new NoteCreateDTO(id, report.fullName(), ""));

        return "patient-report-and-notes";
    }

    /**
     * Displays the form for creating a new patient.
     *
     * @param model the Spring Model object to add attributes for the view
     * @return the view name "patient-form" to render
     */
    @GetMapping("/patients/create")
    public String showCreatePatientForm(Model model) {
        log.info("showCreatePatientForm called");
        model.addAttribute("patient", new PatientCreateDTO());
        return "patient-form";
    }

    /**
     * Creates a new patient.
     *
     * @param patientCreateDTO the patient data to create
     * @param bindingResult the binding result for validation errors
     * @param model the Spring Model object to add attributes for the view
     * @param redirectAttributes attributes for redirect scenarios
     * @return redirect to the newly created patient's page on success, or back to form on failure
     */
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
        return "redirect:/patient/" + id;
    }

    /**
     * Adds a new note for a patient.
     *
     * @param id the patient ID
     * @param noteCreateDTO the note data to create
     * @return redirect to the patient's page
     */
    @PostMapping("/patient/{id}/add-note")
    public String addNote(@PathVariable int id, @Valid NoteCreateDTO noteCreateDTO) {
        log.info("addNote called for: {}", noteCreateDTO);
        gatewayProxy.createNote(noteCreateDTO);
        return "redirect:/patient/" + id;
    }

    /**
     * Deletes a patient's note.
     *
     * @param patientId the patient ID
     * @param noteId the note ID to delete
     * @return redirect to the patient's page
     */
    @PostMapping("/patient/{patientId}/delete-note/{noteId}")
    public String deleteNote(@PathVariable int patientId, @PathVariable String noteId) {
        log.info("deleteNote called for noteId={}", noteId);
        gatewayProxy.deleteNote(noteId);
        return "redirect:/patient/" + patientId;
    }

    /**
     * Updates a patient's information.
     *
     * @param id the patient ID
     * @param patientUpdateDTO the updated patient data
     * @param bindingResult the binding result for validation errors
     * @param model the Spring Model object to add attributes for the view
     * @param redirectAttributes attributes for redirect scenarios
     * @return redirect to the patient's page on success, or back to form on failure
     */
    @PostMapping("/patient/{id}/update")
    public String updatePatient(@PathVariable int id,
                                @Valid PatientUpdateDTO patientUpdateDTO,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        log.info("updatePatient called for: {}", patientUpdateDTO);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la modification du patient");
            return "patient-form";
        }

        gatewayProxy.updatePatient(id, patientUpdateDTO);
        return "redirect:/patient/" + id;
    }

    /**
     * Deletes a patient.
     *
     * @param id the patient ID to delete
     * @return redirect to the home page
     */
    @PostMapping("/patient/{id}/delete")
    public String deletePatient(@PathVariable int id) {
        log.info("deletePatient called");
        gatewayProxy.deletePatient(id);
        return "redirect:/home";
    }
}
