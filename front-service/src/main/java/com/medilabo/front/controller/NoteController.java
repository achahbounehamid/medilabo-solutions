
package com.medilabo.front.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Controller
@RequestMapping("/notes")
public class NoteController {

    private final RestTemplate restTemplate;

    public NoteController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${note.service.url}")
    private String noteServiceUrl; // ex: http://note-service:9002

    // Liste des notes d’un patient
    @GetMapping("/patient/{patientId}")
    public String list(@PathVariable Integer patientId, Model model) {
        ResponseEntity<List> resp = restTemplate.getForEntity(
                noteServiceUrl + "/api/notes/patient/{id}", List.class, patientId);
        model.addAttribute("notes", resp.getBody());
        model.addAttribute("patientId", patientId);
        return "notesListPage";
    }

    // Formulaire d’ajout
    @GetMapping("/add/{patientId}")
    public String addForm(@PathVariable Integer patientId, Model model) {
        model.addAttribute("patientId", patientId);
        return "addNotePage";
    }

    // Soumission d’ajout
    @PostMapping("/add/{patientId}")
    public String create(@PathVariable Integer patientId,
                         @RequestParam String content) {
        Map<String, Object> body = new HashMap<>();
        body.put("patientId", patientId);
        body.put("content", content);
        restTemplate.postForEntity(noteServiceUrl + "/api/notes", body, Void.class);
        return "redirect:/notes/patient/" + patientId;
    }

    // Formulaire de modification
    @GetMapping("/update/{id}")
    public String editForm(@PathVariable String id, Model model) {
        Map note = restTemplate.getForObject(
                noteServiceUrl + "/api/notes/{id}", Map.class, id);
        if (note == null) return "redirect:/homePage";
        model.addAttribute("note", note);
        return "updateNotePage";
    }

    // Soumission de modification
    @PostMapping("/update/{id}")
    public String update(@PathVariable String id,
                         @RequestParam String content,
                         @RequestParam Integer patientId) { // hidden dans le form
        Map<String, Object> body = new HashMap<>();
        body.put("id", id);
        body.put("content", content);
        restTemplate.put(noteServiceUrl + "/api/notes/{id}", body, id);
        return "redirect:/notes/patient/" + patientId;
    }

    // Suppression (si tu as un bouton “Supprimer”)
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id,
                         @RequestParam Integer patientId) {
        restTemplate.delete(noteServiceUrl + "/api/notes/{id}", id);
        return "redirect:/notes/patient/" + patientId;
    }
}
