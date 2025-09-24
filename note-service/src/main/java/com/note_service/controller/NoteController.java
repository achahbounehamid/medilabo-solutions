package com.note_service.controller;

import com.note_service.model.Note;
import com.note_service.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        //  Assure-toi que noteService utilise le repo trié, sinon renvoie ici le tri
        return noteService.getNotesByPatientIdDesc(patientId);
        // (ou directement repo.findByPatientIdOrderByCreatedAtDesc(patientId) si tu n'as pas de service)
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable String id) {
        return noteService.getNoteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Note addNote(@RequestBody Note note) {
        //  NE PAS setter createdAt ici (auditing s'en occupe)
        //  note.setCreatedAt(LocalDateTime.now());  // À supprimer
        return noteService.saveNote(note);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable String id, @RequestBody Note updatedNote) {
        // (Optionnel) 400 si id du body != id du path
        if (updatedNote.getId() != null && !id.equals(updatedNote.getId())) {
            return ResponseEntity.badRequest().build();
        }
        return noteService.getNoteById(id)
                .map(note -> {
                    note.setContent(updatedNote.getContent()); // content-only
                    // updatedAt sera mis à jour par @LastModifiedDate
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
