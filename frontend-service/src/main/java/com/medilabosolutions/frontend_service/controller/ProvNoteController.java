package com.medilabosolutions.frontend_service.controller;

import com.medilabosolutions.frontend_service.dto.NoteDTO;
import com.medilabosolutions.frontend_service.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping()
public class ProvNoteController {

    private final NoteService noteService;

    @GetMapping("/patients/{id}/notes")
    public String getPatientNotes(@PathVariable int id, Model model) {

        List<NoteDTO> notes = noteService.getNotesForPatient(id);

        model.addAttribute("notes", notes);
        return "notes";
    }
}
