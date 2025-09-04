package com.medilabosolutions.note_service.service;

import com.medilabosolutions.note_service.dto.NoteCreateDTO;
import com.medilabosolutions.note_service.dto.NoteDTO;
import com.medilabosolutions.note_service.mapper.NoteMapper;
import com.medilabosolutions.note_service.model.Note;
import com.medilabosolutions.note_service.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private NoteMapper noteMapper;

    @InjectMocks
    private NoteService noteService;

    private Note note1;
    private Note note2;
    private NoteCreateDTO noteCreateDTO;
    private NoteCreateDTO createdNoteDTO;
    private NoteDTO noteDTO1;
    private NoteDTO noteDTO2;

    @BeforeEach
    void setUp() {
        note1 = Note.builder()
                .id("1")
                .patId(1)
                .patient("John Doe")
                .note("Première note de test pour le patient")
                .date(LocalDateTime.of(2024, 1, 15, 10, 30))
                .build();

        note2 = Note.builder()
                .id("2")
                .patId(1)
                .patient("John Doe")
                .note("Deuxième note de test")
                .date(LocalDateTime.of(2024, 1, 16, 14, 45))
                .build();

        noteCreateDTO = new NoteCreateDTO(1, "John Doe", "Nouvelle note de test");

        // NoteCreateDTO retourné après création (sans date dans le record)
        createdNoteDTO = new NoteCreateDTO(1, "John Doe", "Nouvelle note de test");

        // NoteDTO sans date
        noteDTO1 = new NoteDTO("1", 1, "John Doe", "Première note de test pour le patient");
        noteDTO2 = new NoteDTO("2", 1, "John Doe", "Deuxième note de test");
    }

    @Test
    void createNote_ShouldSetCurrentDateAndSaveNote() {
        // Given
        Note noteToSave = Note.builder()
                .patId(1)
                .patient("John Doe")
                .note("Nouvelle note de test")
                .build();

        Note savedNote = Note.builder()
                .id("1")
                .patId(1)
                .patient("John Doe")
                .note("Nouvelle note de test")
                .date(LocalDateTime.now())
                .build();

        when(noteMapper.toEntity(noteCreateDTO)).thenReturn(noteToSave);
        when(noteRepository.save(noteToSave)).thenReturn(savedNote);
        when(noteMapper.toNoteCreateDTO(savedNote)).thenReturn(createdNoteDTO);

        // When
        NoteCreateDTO result = noteService.createNote(noteCreateDTO);

        // Then
        assertThat(result).isEqualTo(createdNoteDTO);
        assertThat(result.patId()).isEqualTo(1);
        assertThat(result.patient()).isEqualTo("John Doe");
        assertThat(result.note()).isEqualTo("Nouvelle note de test");
        assertThat(noteToSave.getDate()).isNotNull(); // La date doit être définie dans l'entité

        verify(noteMapper).toEntity(noteCreateDTO);
        verify(noteRepository).save(noteToSave);
        verify(noteMapper).toNoteCreateDTO(savedNote);
    }

    @Test
    void getAllNotesByPatientId_WhenNotesExist_ShouldReturnListOfNoteDTOs() {
        // Given
        int patientId = 1;
        List<Note> notes = List.of(note1, note2);

        when(noteRepository.findByPatId(patientId)).thenReturn(notes);
        when(noteMapper.toNoteListItemDTO(note1)).thenReturn(noteDTO1);
        when(noteMapper.toNoteListItemDTO(note2)).thenReturn(noteDTO2);

        // When
        List<NoteDTO> result = noteService.getAllNotesByPatientId(patientId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(noteDTO1, noteDTO2);

        // Vérification des détails
        assertThat(result.getFirst().id()).isEqualTo("1");
        assertThat(result.getFirst().patId()).isEqualTo(1);
        assertThat(result.getFirst().patient()).isEqualTo("John Doe");
        assertThat(result.getFirst().note()).isEqualTo("Première note de test pour le patient");

        assertThat(result.get(1).id()).isEqualTo("2");
        assertThat(result.get(1).patId()).isEqualTo(1);
        assertThat(result.get(1).patient()).isEqualTo("John Doe");
        assertThat(result.get(1).note()).isEqualTo("Deuxième note de test");

        verify(noteRepository).findByPatId(patientId);
        verify(noteMapper, times(2)).toNoteListItemDTO(any(Note.class));
    }

    @Test
    void getAllNotesByPatientId_WhenNoNotesExist_ShouldReturnEmptyList() {
        // Given
        int patientId = 999;
        when(noteRepository.findByPatId(patientId)).thenReturn(List.of());

        // When
        List<NoteDTO> result = noteService.getAllNotesByPatientId(patientId);

        // Then
        assertThat(result).isEmpty();
        verify(noteRepository).findByPatId(patientId);
        verify(noteMapper, never()).toNoteListItemDTO(any());
    }

    @Test
    void getAllNotesByPatientId_WithDifferentPatientId_ShouldReturnOnlyMatchingNotes() {
        // Given
        int patientId = 2;
        Note noteForPatient2 = Note.builder()
                .id("3")
                .patId(2)
                .patient("Jane Smith")
                .note("Note pour patient 2")
                .date(LocalDateTime.of(2024, 1, 17, 11, 0))
                .build();

        NoteDTO noteDTOForPatient2 = new NoteDTO("3", 2, "Jane Smith", "Note pour patient 2");

        when(noteRepository.findByPatId(patientId)).thenReturn(List.of(noteForPatient2));
        when(noteMapper.toNoteListItemDTO(noteForPatient2)).thenReturn(noteDTOForPatient2);

        // When
        List<NoteDTO> result = noteService.getAllNotesByPatientId(patientId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().id()).isEqualTo("3");
        assertThat(result.getFirst().patId()).isEqualTo(2);
        assertThat(result.getFirst().patient()).isEqualTo("Jane Smith");
        assertThat(result.getFirst().note()).isEqualTo("Note pour patient 2");

        verify(noteRepository).findByPatId(patientId);
        verify(noteMapper).toNoteListItemDTO(noteForPatient2);
    }

    @Test
    void getAllNotesByPatientId_WithNullPatientName_ShouldHandleNullValues() {
        // Given
        int patientId = 3;
        Note noteWithNullPatient = Note.builder()
                .id("4")
                .patId(3)
                .patient(null)
                .note("Note sans nom de patient")
                .date(LocalDateTime.now())
                .build();

        NoteDTO noteDTOWithNullPatient = new NoteDTO("4", 3, null, "Note sans nom de patient");

        when(noteRepository.findByPatId(patientId)).thenReturn(List.of(noteWithNullPatient));
        when(noteMapper.toNoteListItemDTO(noteWithNullPatient)).thenReturn(noteDTOWithNullPatient);

        // When
        List<NoteDTO> result = noteService.getAllNotesByPatientId(patientId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().patient()).isNull();
        assertThat(result.getFirst().note()).isEqualTo("Note sans nom de patient");

        verify(noteRepository).findByPatId(patientId);
        verify(noteMapper).toNoteListItemDTO(noteWithNullPatient);
    }

    @Test
    void deleteAllNotes_ShouldCallRepositoryDeleteAll() {
        // When
        noteService.deleteAllNotes();

        // Then
        verify(noteRepository).deleteAll();
        verifyNoMoreInteractions(noteRepository);
    }

    @Test
    void createNote_ShouldSetCurrentDateTimeAutomatically() {
        // Given
        LocalDateTime beforeTest = LocalDateTime.now().minusSeconds(1);

        Note noteToSave = Note.builder()
                .patId(1)
                .patient("John Doe")
                .note("Test date automatique")
                .build();

        Note savedNote = Note.builder()
                .id("1")
                .patId(1)
                .patient("John Doe")
                .note("Test date automatique")
                .date(LocalDateTime.now())
                .build();

        NoteCreateDTO expectedDTO = new NoteCreateDTO(1, "John Doe", "Test date automatique");

        when(noteMapper.toEntity(noteCreateDTO)).thenReturn(noteToSave);
        when(noteRepository.save(noteToSave)).thenReturn(savedNote);
        when(noteMapper.toNoteCreateDTO(savedNote)).thenReturn(expectedDTO);

        // When
        NoteCreateDTO result = noteService.createNote(noteCreateDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.patId()).isEqualTo(1);
        assertThat(result.patient()).isEqualTo("John Doe");
        assertThat(result.note()).isEqualTo("Test date automatique");
        assertThat(noteToSave.getDate()).isNotNull();
        assertThat(noteToSave.getDate()).isAfterOrEqualTo(beforeTest);

        verify(noteRepository).save(noteToSave);
    }

    @Test
    void createNote_WithEmptyPatientName_ShouldWorkCorrectly() {
        // Given
        NoteCreateDTO createDTOWithEmptyPatient = new NoteCreateDTO(1, "", "Note avec patient vide");

        Note noteToSave = Note.builder()
                .patId(1)
                .patient("")
                .note("Note avec patient vide")
                .build();

        Note savedNote = Note.builder()
                .id("1")
                .patId(1)
                .patient("")
                .note("Note avec patient vide")
                .date(LocalDateTime.now())
                .build();

        NoteCreateDTO expectedDTO = new NoteCreateDTO(1, "", "Note avec patient vide");

        when(noteMapper.toEntity(createDTOWithEmptyPatient)).thenReturn(noteToSave);
        when(noteRepository.save(noteToSave)).thenReturn(savedNote);
        when(noteMapper.toNoteCreateDTO(savedNote)).thenReturn(expectedDTO);

        // When
        NoteCreateDTO result = noteService.createNote(createDTOWithEmptyPatient);

        // Then
        assertThat(result).isEqualTo(expectedDTO);
        assertThat(result.patient()).isEmpty();
        verify(noteRepository).save(noteToSave);
    }
}