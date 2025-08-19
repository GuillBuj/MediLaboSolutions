package com.medilabosolutions.frontend_service.service;

import com.medilabosolutions.frontend_service.dto.NoteDTO;
import com.medilabosolutions.frontend_service.dto.ReportDTO;
import com.medilabosolutions.frontend_service.proxy.NoteProxy;
import com.medilabosolutions.frontend_service.proxy.ReportProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {

    private final ReportProxy reportProxy;

     public ReportDTO getPatientReport(int patientId) {
        log.info("Fetching notes for patient: {}", patientId);
        try {
            return reportProxy.getPatientReport(patientId);
        } catch (Exception e) {
            log.error("Error fetching report: ", e);
            throw e;
        }
    }

}
