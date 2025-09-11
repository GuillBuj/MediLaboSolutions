package com.medilabosolutions.note_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabosolutions.note_service.dto.NoteCreateDTO;
import com.medilabosolutions.note_service.dto.NoteDTO;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NoteControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    private NoteCreateDTO sampleNote;

    private MongodExecutable mongodExecutable;

    @BeforeAll
    void setUpEmbeddedMongo() throws IOException {
        int port = 27017;
        MongodConfig mongodConfig = MongodConfig.builder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(port, Network.localhostIsIPv6()))
                .build();
        mongodExecutable = MongodStarter.getDefaultInstance().prepare(mongodConfig);
        mongodExecutable.start();
    }

    @AfterAll
    void stopEmbeddedMongo() {
        if (mongodExecutable != null) {
            mongodExecutable.stop();
        }
    }

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection("note");
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
        mongoTemplate.save(sampleNote, "note");

        mockMvc.perform(get("/api/notes/patient/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patId").value(1))
                .andExpect(jsonPath("$[0].note").value("Note de test"));
    }

    @Test
    void deleteNote_ShouldRemoveNote() throws Exception {
        NoteDTO savedNote = objectMapper.readValue(
                mockMvc.perform(post("/api/notes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sampleNote)))
                        .andReturn().getResponse().getContentAsString(),
                NoteDTO.class
        );

        mockMvc.perform(delete("/api/notes/{id}", savedNote.id()))
                .andExpect(status().isNoContent());

        List<NoteDTO> remainingNotes = objectMapper.readValue(
                mockMvc.perform(get("/api/notes/patient/1"))
                        .andReturn().getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, NoteDTO.class)
        );

        assertThat(remainingNotes).isEmpty();
    }

    @Test
    void deleteAllNotes_ShouldRemoveAllNotes() throws Exception {
        mongoTemplate.save(sampleNote, "note");
        mongoTemplate.save(new NoteCreateDTO(2, "Marie Dupont", "Autre note"), "note");

        mockMvc.perform(get("/api/notes/delete-all"))
                .andExpect(status().isOk());

        long count = mongoTemplate.getCollection("note").countDocuments();
        assertThat(count).isZero();
    }
}
