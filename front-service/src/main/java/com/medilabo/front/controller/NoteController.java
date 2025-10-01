package com.medilabo.front.controller;

import com.medilabo.front.model.Note;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private final RestTemplate restTemplate;

    public NoteController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${note.service.url}")
    private String noteServiceUrl;

    // Liste des notes d’un patient (type fort)
    @GetMapping("/patient/{patientId}")
    public String list(@PathVariable Integer patientId, Model model) {
        ResponseEntity<List<Note>> resp = restTemplate.exchange(
                noteServiceUrl + "/api/notes/patient/{id}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Note>>() {},
                patientId
        );
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
//    @PostMapping("/add/{patientId}")
//    public String create(@PathVariable Integer patientId,
//                         @RequestParam String content,
//                         RedirectAttributes ra) {
//        try {
//            Map<String, Object> body = new HashMap<>();
//            body.put("patientId", patientId);
//            body.put("content", content);
//
//            // Si tu veux forcer JSON :
//            // var headers = new HttpHeaders(); headers.setContentType(MediaType.APPLICATION_JSON);
//            // restTemplate.postForEntity(noteServiceUrl + "/api/notes", new HttpEntity<>(body, headers), Void.class);
//
//            restTemplate.postForEntity(noteServiceUrl + "/api/notes", body, Void.class);
//            ra.addFlashAttribute("successMessage", "Note ajoutée avec succès.");
//        } catch (Exception e) {
//            ra.addFlashAttribute("errorMessage", "Impossible d’ajouter la note.");
//        }
//        return "redirect:/patient/infos/" + patientId;
//    }


    // Formulaire de modification (type fort)
    @GetMapping("/update/{id}")
    public String editForm(@PathVariable String id, Model model) {
        Note note = restTemplate.getForObject(
                noteServiceUrl + "/api/notes/{id}", Note.class, id);
        if (note == null) return "redirect:/homePage";
        model.addAttribute("note", note);
        return "updateNotePage";
    }

    // Soumission de modification
    @PostMapping("/update/{id}")
    public String update(@PathVariable String id,
                         @RequestParam String content,
                         @RequestParam Integer patientId) {
        Map<String, Object> body = new HashMap<>();
        body.put("id", id);
        body.put("content", content);
        restTemplate.put(noteServiceUrl + "/api/notes/{id}", body, id);
        return "redirect:/notes/patient/" + patientId;
    }

    // Suppression
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id,
                         @RequestParam Integer patientId) {
        restTemplate.delete(noteServiceUrl + "/api/notes/{id}", id);
        return "redirect:/notes/patient/" + patientId;
    }
}
