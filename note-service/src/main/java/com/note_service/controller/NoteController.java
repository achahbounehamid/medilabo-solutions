package com.note_service.controller;

import com.note_service.model.Note;
import com.note_service.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    @GetMapping("/patient/{patientId}")
    public List<Note> getNotesByPatient(@PathVariable Integer patientId) {
        return noteService.getNotesByPatientId(patientId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable String id) {
        return noteService.getNoteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Note addNote(@RequestBody Note note) {
        note.setCreatedAt(LocalDateTime.now());
        return noteService.saveNote(note);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable String id, @RequestBody Note updatedNote) {
        return noteService.getNoteById(id)
                .map(note -> {
                    note.setContent(updatedNote.getContent());
                    return ResponseEntity.ok(noteService.saveNote(note));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable String id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}