package com.medilabo.front.controller;


import org.springframework.ui.Model;
import com.medilabo.front.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Controller
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${note.service.url}")
    private String noteServiceUrl;

    // GET : Liste des notes par patient
    @GetMapping("/patient/{patientId}")
    public String getNotesByPatient(@PathVariable Integer patientId, Model model) {
        ResponseEntity<Note[]> response = restTemplate.getForEntity(
                noteServiceUrl + "/api/notes/patient/" + patientId, Note[].class);
        model.addAttribute("notes", Arrays.asList(response.getBody()));
        model.addAttribute("patientId", patientId);
        return "noteListPage";
    }

    // GET : Formulaire pour ajouter une note
    @GetMapping("/add/{patientId}")
    public String showAddNoteForm(@PathVariable Integer patientId, Model model) {
        model.addAttribute("patientId", patientId);
        return "addNotePage";
    }

    // POST : Soumettre la note
    @PostMapping("/add/{patientId}")
    public String addNote(@PathVariable Integer patientId, @RequestParam String content) {
        Note note = new Note();
        note.setPatientId(patientId);
        note.setContent(content);
        note.setCreatedAt(LocalDateTime.now());

        restTemplate.postForEntity(noteServiceUrl + "/api/notes", note, Note.class);
        return "redirect:/notes/patient/" + patientId;
    }

    @GetMapping("/update/{id}")
    public String showUpdateNoteForm(@PathVariable String id, Model model) {
        ResponseEntity<Note> response = restTemplate.getForEntity(noteServiceUrl + "/api/notes/" + id, Note.class);
        model.addAttribute("note", response.getBody());
        return "updateNotePage";
    }

    @PostMapping("/update/{id}")
    public String updateNote(@PathVariable String id,
                             @RequestParam String content,
                             @RequestParam Integer patientId) {
        Note updatedNote = new Note();
        updatedNote.setContent(content);
        updatedNote.setPatientId(patientId);

        restTemplate.put(noteServiceUrl + "/api/notes/" + id, updatedNote);
        return "redirect:/notes/patient/" + patientId;
    }
    @GetMapping("/delete/{id}")
    public String deleteNote(@PathVariable String id,
                             @RequestParam Integer patientId) {
        restTemplate.delete(noteServiceUrl + "/api/notes/" + id);
        return "redirect:/notes/patient/" + patientId;
    }


}
