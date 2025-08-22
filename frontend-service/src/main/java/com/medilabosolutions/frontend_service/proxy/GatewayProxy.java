package com.medilabosolutions.frontend_service.proxy;

import com.medilabosolutions.frontend_service.config.FeignClientConfig;
import com.medilabosolutions.frontend_service.config.FeignErrorDecoder;
import com.medilabosolutions.frontend_service.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "gateway",
        url = "${gateway.url}",
        configuration = {FeignClientConfig.class, FeignErrorDecoder.class}
)
public interface GatewayProxy {

    @GetMapping("/api/patients")
    List<PatientDTO> getAllPatients();

    @GetMapping("/api/patients/{patientId}")
    PatientDTO getPatient(@PathVariable int patientId);

    @GetMapping("/api/report/{patientId}")
    ReportDTO getPatientReport(@PathVariable int patientId);

    @GetMapping("/api/notes/patient/{patientId}")
    List<NoteDTO> getPatientNotes(@PathVariable int patientId);

    @DeleteMapping("/api/patients/{patientId}")
    void deletePatient(@PathVariable int patientId);

    @PostMapping("/api/patients")
    PatientDTO createPatient(PatientCreateDTO patient);

    @PostMapping("/api/notes")
    NoteDTO createNote(NoteCreateDTO note);

}

