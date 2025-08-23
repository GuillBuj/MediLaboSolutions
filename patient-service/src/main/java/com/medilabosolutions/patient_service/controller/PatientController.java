package com.medilabosolutions.patient_service.controller;

import com.medilabosolutions.patient_service.dto.PatientCreateDTO;
import com.medilabosolutions.patient_service.dto.PatientDTO;
import com.medilabosolutions.patient_service.dto.PatientUpdateDTO;
import com.medilabosolutions.patient_service.service.PatientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for handling patient-related HTTP requests.
 * Exposes API endpoints for CRUD operations on patient resources.
 */
@RestController
@RequestMapping("api/patients")
@AllArgsConstructor
@Slf4j
public class PatientController {
    private final PatientService patientService;

    /**
     * Creates a new patient resource.
     * POST /api/patients
     *
     * @param patientCreateDTO The patient data transfer object containing patient details
     * @return ResponseEntity containing the created PatientDTO with HTTP status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody PatientCreateDTO patientCreateDTO) {
        log.info("Creating patient {}", patientCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(patientCreateDTO));
    }

    /**
     * Retrieves all patients from the system.
     * GET /api/patients
     *
     * @return ResponseEntity containing a list of PatientDTOs with HTTP status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    /**
     * Retrieves a specific patient by their ID.
     * GET /api/patients/{id}
     *
     * @param id The ID of the patient to retrieve
     * @return ResponseEntity containing the PatientDTO with HTTP status 200 (OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    /**
     * Updates an existing patient resource.
     * PUT /api/patients/{id}
     *
     * @param id The ID of the patient to update
     * @param patientUpdateDTO The updated patient data
     * @return ResponseEntity containing the updated PatientUpdateDTO with HTTP status 200 (OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<PatientUpdateDTO> updatePatient(@PathVariable Long id, @RequestBody PatientUpdateDTO patientUpdateDTO) {
        log.info("Updating patient {}", patientUpdateDTO);
        return ResponseEntity.ok(patientService.updatePatient(id, patientUpdateDTO));
    }

    /**
     * Deletes a patient resource by ID.
     * DELETE /api/patients/{id}
     *
     * @param id The ID of the patient to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }

    //todo: supp
    /**
     * Deletes all patients from the system (primarily for testing purposes).
     * GET /api/patients/delete-all
     * Use with caution as this operation is irreversible.
     */
    @GetMapping("/delete-all")
    public void deleteAllPatients() {
        patientService.deleteAllPatients();
    }
}
