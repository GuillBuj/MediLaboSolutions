package com.medilabosolutions.frontend_service.proxy;

import com.medilabosolutions.frontend_service.config.FeignClientConfig;
import com.medilabosolutions.frontend_service.config.FeignErrorDecoder;
import com.medilabosolutions.frontend_service.dto.ReportDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "report-service",
        url = "${report.url}",
        configuration = {FeignClientConfig.class, FeignErrorDecoder.class}
)
public interface ProvReportProxy {

    @GetMapping("/api/report/{patientId}")
    ReportDTO getPatientReport(@PathVariable int patientId);
}