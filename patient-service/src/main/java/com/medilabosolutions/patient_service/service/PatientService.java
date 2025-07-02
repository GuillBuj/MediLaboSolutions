package com.medilabosolutions.patient_service.service;

import com.medilabosolutions.patient_service.dto.PatientCreateDTO;
import com.medilabosolutions.patient_service.dto.PatientListItemDTO;
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

@Service
@AllArgsConstructor
@Slf4j
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientCreateDTO savePatient(PatientCreateDTO patientCreateDTO) {
        log.info("--- Saving patient {}", patientCreateDTO);
        Patient savedPatient = patientRepository.save(patientMapper.toEntity(patientCreateDTO));
        return patientMapper.toPatientCreateDTO(savedPatient);
    }

    public PatientUpdateDTO updatePatient(PatientUpdateDTO patientUpdateDTO) {
        log.info("--- Updating patient {}", patientUpdateDTO);
        Patient updatedPatient = patientRepository.save(patientMapper.toEntity(patientUpdateDTO));
        return patientMapper.toPatientUpdateDTO(updatedPatient);
    }

    public void deletePatient(Long id) {
        log.info("--- Deleting patient {}", id);
        patientRepository.deleteById(id);
    }

    public PatientListItemDTO getPatientById(Long id) {
        log.info("--- Getting patient {}", id);
        return patientRepository.findById(id)
                .map(patientMapper::toPatientListItemDTO)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    public List<PatientListItemDTO> getAllPatients() {
        log.info("--- Getting patients");
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(patientMapper::toPatientListItemDTO)
                .collect(Collectors.toList());
    }
}
