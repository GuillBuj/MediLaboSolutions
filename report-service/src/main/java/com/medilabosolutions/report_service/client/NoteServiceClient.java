package com.medilabosolutions.report_service.client;


import com.medilabosolutions.report_service.dto.NoteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "note-service", url = "${note.service.url:http://note-service:8081}")
public interface NoteServiceClient {

    @GetMapping("/api/notes/patient/{id}")
    ResponseEntity<List<NoteDTO>> getNotesByPatientId(@PathVariable(name = "id") long id);
}
