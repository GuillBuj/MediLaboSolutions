package com.medilabosolutions.frontend_service.controller;

import com.medilabosolutions.frontend_service.dto.NoteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NoteController {

    private final WebClient webClient;

    @GetMapping("/patients/{id}/notes")
    public String getPatientNotes(@PathVariable Long id, Model model) {
        List<NoteDTO> notes = webClient.get()
                .uri("/api/notes/patient/{id}", id)
                .retrieve()
                .bodyToFlux(NoteDTO.class)
                .collectList()
                .block();

        model.addAttribute("notes", notes);
        return "notes";
    }
}
