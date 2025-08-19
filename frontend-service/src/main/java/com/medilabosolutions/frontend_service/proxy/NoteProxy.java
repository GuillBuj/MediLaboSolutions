package com.medilabosolutions.frontend_service.proxy;

import com.medilabosolutions.frontend_service.config.FeignClientConfig;
import com.medilabosolutions.frontend_service.config.FeignErrorDecoder;
import com.medilabosolutions.frontend_service.dto.NoteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

//@FeignClient(name = "gateway-service",
//            url = "${gateway.url}",
//            configuration = {FeignClientConfig.class, FeignErrorDecoder.class}
//)
@FeignClient(name = "note-service",
        url = "${note.url}",
        configuration = {FeignClientConfig.class, FeignErrorDecoder.class}
)
public interface NoteProxy {

    @GetMapping("/api/notes/patient/{patientId}")
    List<NoteDTO> getPatientHistory(@PathVariable int patientId);
}