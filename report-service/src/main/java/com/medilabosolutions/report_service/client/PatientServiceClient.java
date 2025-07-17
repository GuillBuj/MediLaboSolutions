package com.medilabosolutions.report_service.client;


import com.medilabosolutions.report_service.dto.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-service", url = "http://patient-service:8080")
public interface PatientServiceClient {

    @GetMapping("/api/patients/{id}")
    ResponseEntity<PatientDTO> getPatientById(@PathVariable(name = "id") long id);
}
