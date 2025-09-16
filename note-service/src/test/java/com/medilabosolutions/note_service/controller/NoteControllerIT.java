package com.medilabosolutions.note_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabosolutions.note_service.dto.NoteCreateDTO;

import com.medilabosolutions.note_service.model.Note;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.MongoDBContainer;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NoteControllerIT {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:5.0");

    static {
        // S'assure que le conteneur démarre avant Spring Boot
        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // Utiliser getReplicaSetUrl() ou getConnectionString()
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    private NoteCreateDTO sampleNote;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection("notes");
        sampleNote = new NoteCreateDTO(1, "Jean Dupont", "Note de test");
    }

    @Test
    void createNote_ShouldReturnCreatedNote() throws Exception {
        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleNote)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patId").value(1))
                .andExpect(jsonPath("$.note").value("Note de test"));
    }

    @Test
    void getAllNotesByPatientId_ShouldReturnListOfNotes() throws Exception {
        mongoTemplate.save(sampleNote, "notes");

        mockMvc.perform(get("/api/notes/patient/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patId").value(1))
                .andExpect(jsonPath("$[0].note").value("Note de test"));
    }

    @Test
    void deleteNote_ShouldRemoveNote() throws Exception {
        // Préparer la note directement dans MongoDB
        Note note = new Note();
        note.setPatId(1);
        note.setPatient("Jean Dupont");
        note.setNote("Note de test");
        note.setDate(LocalDateTime.now());

        // MongoDB va générer l'ID automatiquement
        note = mongoTemplate.save(note, "notes");

        // Vérifier que l'ID est bien présent
        assertThat(note.getId()).isNotNull();

        // Appel du DELETE avec l'ID réel
        mockMvc.perform(delete("/api/notes/{id}", note.getId()))
                .andExpect(status().isNoContent());

        // Vérifier que la note a bien été supprimée
        List<Note> remainingNotes = mongoTemplate.find(
                Query.query((CriteriaDefinition) Criteria.where("patId").is(1)),
                Note.class,
                "notes"
        );
        assertThat(remainingNotes).isEmpty();
    }


    @Test
    void deleteAllNotes_ShouldRemoveAllNotes() throws Exception {
        mongoTemplate.save(new Note("1", 1, "Jean Dupont", "Note de test", LocalDateTime.now()), "notes");
        mongoTemplate.save(new Note("2", 2, "Marie Dupont", "Autre note", LocalDateTime.now()), "notes");

        mockMvc.perform(get("/api/notes/delete-all"))
                .andExpect(status().isOk());

        long count = mongoTemplate.getCollection("notes").countDocuments();
        assertThat(count).isZero();
    }
}
