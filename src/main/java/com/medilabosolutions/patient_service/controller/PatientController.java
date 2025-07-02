package com.medilabosolutions.patient_service.controller;

import com.medilabosolutions.patient_service.dto.PatientCreateDTO;
import com.medilabosolutions.patient_service.dto.PatientListItemDTO;
import com.medilabosolutions.patient_service.dto.PatientUpdateDTO;
import com.medilabosolutions.patient_service.model.Patient;
import com.medilabosolutions.patient_service.service.PatientService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/patients")
@AllArgsConstructor
@Slf4j
public class PatientController {
    private final PatientService patientService;

    //TODO validation
    @PostMapping
    public ResponseEntity<PatientCreateDTO> createPatient(@RequestBody PatientCreateDTO patientCreateDTO) {
        log.info("Creating patient {}", patientCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.savePatient(patientCreateDTO));
    }

    @GetMapping
    public ResponseEntity<List<PatientListItemDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @PutMapping
    public ResponseEntity<PatientUpdateDTO> updatePatient(@RequestBody PatientUpdateDTO patientUpdateDTO) {
        log.info("Updating patient {}", patientUpdateDTO);
        return ResponseEntity.ok(patientService.updatePatient(patientUpdateDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }

}
