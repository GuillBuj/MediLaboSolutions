package com.medilabosolutions.frontend_service.proxy;

import com.medilabosolutions.frontend_service.config.FeignClientConfig;
import com.medilabosolutions.frontend_service.config.FeignErrorDecoder;
import com.medilabosolutions.frontend_service.dto.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@FeignClient(name = "patient-service",
        url = "${patient.url}",
        configuration = {FeignClientConfig.class, FeignErrorDecoder.class}
)
public interface ProvPatientProxy {

    @GetMapping("/api/patients")
    List<PatientDTO> getAllPatients();
}