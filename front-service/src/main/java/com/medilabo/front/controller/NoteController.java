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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Contrôleur MVC chargé de la gestion des notes côté front-service.
 *
 * <p>
 * Orchestration des pages listant, ajoutant, modifiant et supprimant des notes,
 * via des appels REST au <b>note-service</b> à l'aide d'un {@link RestTemplate}.
 * </p>
 *
 * <p>Vues utilisées : <code>notesListPage</code>, <code>addNotePage</code>, <code>updateNotePage</code>.</p>
 */
@Controller
@RequestMapping("/notes")
public class NoteController {
    /** Client HTTP pour appeler le note-service. */
    private final RestTemplate restTemplate;

    public NoteController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    /** URL de base du note-service (ex. http://localhost:8082). */
    @Value("${note.service.url}")
    private String noteServiceUrl;
    /**
     * Liste les notes d'un patient donné.
     * <p>Appelle <code>GET {noteServiceUrl}/api/notes/patient/{id}</code>.</p>
     *
     * @param patientId identifiant du patient
     * @param model     modèle de vue (attributs : <code>notes</code>, <code>patientId</code>)
     * @return la vue <code>notesListPage</code>
     */
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
    /**
     * Affiche le formulaire d'ajout d'une note pour un patient.
     *
     * @param patientId identifiant du patient
     * @param model     modèle de vue (attribut <code>patientId</code>)
     * @return la vue <code>addNotePage</code>
     */
    // Formulaire d’ajout
    @GetMapping("/add/{patientId}")
    public String addForm(@PathVariable Integer patientId, Model model) {
        model.addAttribute("patientId", patientId);
        return "addNotePage";
    }
    /**
     * Traite la soumission d'ajout d'une note.
     * <p>Appelle <code>POST {noteServiceUrl}/api/notes</code>.</p>
     *
     * @param patientId identifiant du patient
     * @param content   contenu de la note
     * @return redirection vers la fiche patient ou la liste des notes
     */
    // Soumission d’ajout
    @PostMapping("/add/{patientId}")
    public String create(@PathVariable Integer patientId,
                         @RequestParam String content) {
        Map<String, Object> body = new HashMap<>();
        body.put("patientId", patientId);
        body.put("content", content);
        restTemplate.postForEntity(noteServiceUrl + "/api/notes", body, Void.class);
//        return "redirect:/notes/patient/" + patientId;
        return "redirect:/patient/infos/" + patientId;

    }

    /**
     * Affiche le formulaire d'édition d'une note existante.
     * <p>Appelle <code>GET {noteServiceUrl}/api/notes/{id}</code>.</p>
     *
     * @param id    identifiant de la note (chaîne, côté note-service)
     * @param model modèle de vue (attribut <code>note</code>)
     * @return la vue <code>updateNotePage</code> ou redirection vers l'accueil si note absente
     */
    // Formulaire de modification (type fort)
    @GetMapping("/update/{id}")
    public String editForm(@PathVariable String id, Model model) {
        Note note = restTemplate.getForObject(
                noteServiceUrl + "/api/notes/{id}", Note.class, id);
        if (note == null) return "redirect:/homePage";
        model.addAttribute("note", note);
        return "updateNotePage";


    }
    /**
     * Traite la soumission de modification d'une note.
     * <p>Appelle <code>PUT {noteServiceUrl}/api/notes/{id}</code>.</p>
     *
     * @param id        identifiant de la note
     * @param content   nouveau contenu
     * @param patientId identifiant du patient (utilisé pour la redirection)
     * @return redirection vers la liste des notes du patient
     */
    // Soumission de modification
    @PostMapping("/update/{id}")
    public String update(@PathVariable String id,
                         @RequestParam String content,
                         @RequestParam Integer patientId) {
        Map<String, Object> body = new HashMap<>();
        body.put("id", id);
        body.put("content", content);
        restTemplate.put(noteServiceUrl + "/api/notes/{id}", body, id);
//        return "redirect:/notes/patient/" + patientId;
        return "redirect:/patient/infos/" + patientId;
    }
    /**
     * Supprime une note, puis redirige vers la fiche du patient.
     * <p>Appelle <code>DELETE {noteServiceUrl}/api/notes/{id}</code>.</p>
     *
     * @param id        identifiant de la note
     * @param patientId identifiant du patient (pour la redirection)
     * @return redirection vers <code>/patient/infos/{patientId}</code>
     */
    // Suppression
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id,
                         @RequestParam Integer patientId) {
        restTemplate.delete(noteServiceUrl + "/api/notes/{id}", id);
//        return "redirect:/notes/patient/" + patientId;
        return "redirect:/patient/infos/" + patientId;

    }
}