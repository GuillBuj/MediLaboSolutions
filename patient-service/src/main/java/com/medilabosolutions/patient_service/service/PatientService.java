package com.medilabosolutions.patient_service.service;

import com.medilabosolutions.patient_service.dto.PatientCreateDTO;
import com.medilabosolutions.patient_service.dto.PatientDTO;
import com.medilabosolutions.patient_service.dto.PatientUpdateDTO;
import com.medilabosolutions.patient_service.exception.PatientNotFoundException;
import com.medilabosolutions.patient_service.mapper.PatientMapper;
import com.medilabosolutions.patient_service.model.Patient;
import com.medilabosolutions.patient_service.repository.PatientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Business service for managing patient entities.
 * Provides comprehensive CRUD (Create, Read, Update, Delete) operations for patients.
 * Includes proper validation and exception handling for patient operations.
*/
@Service
@AllArgsConstructor
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    /**
     * Creates a new patient from the provided data.
     *
     * @param patientCreateDTO DTO containing data required to create a patient
     * @return PatientDTO representing the created patient with its generated identifier
     */
    public PatientDTO createPatient(PatientCreateDTO patientCreateDTO) {
        log.info("--- Creating patient {}", patientCreateDTO);

        Patient savedPatient = patientRepository.save(patientMapper.toEntity(patientCreateDTO));

        log.info("--- Patient {} created", savedPatient);
        return patientMapper.toPatientDTO(savedPatient);
    }

    /**
     * Updates an existing patient with the provided data.
     * Validates that the patient exists before attempting update.
     *
     * @param id The ID of the patient to update
     * @param patientUpdateDTO DTO containing updated patient data
     * @return PatientUpdateDTO representing the updated patient
     * @throws PatientNotFoundException if no patient is found with the given ID
     */
    public PatientUpdateDTO updatePatient(Long id, PatientUpdateDTO patientUpdateDTO) {
        log.info("--- Updating patient {}", patientUpdateDTO);

        if (!patientRepository.existsById(id)) {
            throw new PatientNotFoundException(id);
        }
        Patient updatedPatient = patientRepository.save(patientMapper.toEntity(id, patientUpdateDTO));

        log.info("--- Patient {} updated", updatedPatient);
        return patientMapper.toPatientUpdateDTO(updatedPatient);
    }

    /**
     * Deletes a patient by their ID.
     * Validates that the patient exists before attempting deletion.
     *
     * @param id The ID of the patient to delete
     * @throws PatientNotFoundException if no patient is found with the given ID
     */
    public void deletePatient(Long id) {
        log.info("--- Deleting patient {}", id);

        if (!patientRepository.existsById(id)) {
             throw new PatientNotFoundException(id);
        }
        patientRepository.deleteById(id);

        log.info("--- Patient {} deleted", id);
    }

    /**
     * Retrieves a patient by their ID.
     *
     * @param id The ID of the patient to retrieve
     * @return PatientDTO representing the found patient
     * @throws PatientNotFoundException if no patient is found with the given ID
     */
    public PatientDTO getPatientById(Long id) {
        log.info("--- Getting patient {}", id);
        return patientRepository.findById(id)
                .map(patientMapper::toPatientDTO)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    /**
     * Retrieves all patients in the system.
     *
     * @return List of PatientDTOs representing all patients
     *         Returns an empty list if no patients exist
     */
    public List<PatientDTO> getAllPatients() {
        log.info("--- Getting patients");
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(patientMapper::toPatientDTO)
                .collect(Collectors.toList());
    }

    /**
     * Deletes all patients from the system.
     * Use with extreme caution as this operation cannot be undone.
     * Mainly intended for testing or administrative purposes.
     */
    public void deleteAllPatients() {
        log.info("--- Deleting all patients");
        patientRepository.deleteAll();
        log.info("--- All patients deleted");
    }
}
