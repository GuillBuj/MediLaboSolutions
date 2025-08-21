package com.medilabosolutions.frontend_service.service;

import com.medilabosolutions.frontend_service.dto.PatientDTO;
import com.medilabosolutions.frontend_service.proxy.ProvPatientProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PatientService {

    private final ProvPatientProxy patientProxy;

    public List<PatientDTO> getAllPatients() {
        log.info("Fetching all patients");
        try {
            return patientProxy.getAllPatients();
        } catch (Exception e) {
            log.error("Error fetching report: ", e);
            throw e;
        }
    }

}
