package com.medilabosolutions.note_service.repository;

import com.medilabosolutions.note_service.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NoteRepository extends MongoRepository<Note, String> {

    List<Note> findByPatId(Long patId);
}
