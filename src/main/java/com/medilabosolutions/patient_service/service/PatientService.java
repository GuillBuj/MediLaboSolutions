package com.medilabosolutions.patient_service.service;

import com.medilabosolutions.patient_service.dto.PatientCreateDTO;
import com.medilabosolutions.patient_service.mapper.PatientMapper;
import com.medilabosolutions.patient_service.model.Patient;
import com.medilabosolutions.patient_service.repository.PatientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientCreateDTO savePatient(PatientCreateDTO patientCreateDTO) {
        log.info("Saving patient {}", patientCreateDTO);
        Patient savedPatient = patientRepository.save(patientMapper.toEntity(patientCreateDTO));
        return patientMapper.toPatientCreateDTO(savedPatient);
    }
}
