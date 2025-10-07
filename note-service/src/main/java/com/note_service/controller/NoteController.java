package com.note_service.controller;

import com.note_service.model.Note;
import com.note_service.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * Contrôleur REST du microservice <b>note-service</b>.
 *
 * <p>
 * Expose les endpoints CRUD pour gérer les notes médicales et des endpoints de lecture
 * filtrés par patient. Le contrôleur délègue la logique métier et l'accès aux données
 * au {@link NoteService}.
 * </p>
 *
 * <h2>Endpoints</h2>
 * <ul>
 *   <li><code>GET /api/notes</code> — lister toutes les notes</li>
 *   <li><code>GET /api/notes/patient/{patientId}</code> — lister les notes d’un patient (tri décroissant recommandé)</li>
 *   <li><code>GET /api/notes/{id}</code> — récupérer une note par son identifiant</li>
 *   <li><code>POST /api/notes</code> — créer une nouvelle note</li>
 *   <li><code>PUT /api/notes/{id}</code> — mettre à jour le contenu d’une note</li>
 *   <li><code>DELETE /api/notes/{id}</code> — supprimer une note</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/notes")
public class NoteController {
    /** Service applicatif pour la gestion des notes. */
    @Autowired
    private NoteService noteService;
    /**
     * Retourne la liste de toutes les notes.
     *
     * @return liste complète des notes
     */
    @GetMapping
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }
    /**
     * Retourne les notes associées à un patient donné.
     * <p>
     * Remarque : l’ordre recommandé est décroissant sur la date de création (dernières d’abord).
     * </p>
     *
     * @param patientId identifiant du patient
     * @return liste des notes du patient (potentiellement vide)
     */
    @GetMapping("/patient/{patientId}")
    public List<Note> getNotesByPatient(@PathVariable Integer patientId) {
        //  Assure-toi que noteService utilise le repo trié, sinon renvoie ici le tri
        return noteService.getNotesByPatientIdDesc(patientId);
        // (ou directement repo.findByPatientIdOrderByCreatedAtDesc(patientId) si tu n'as pas de service)
    }
    /**
     * Récupère une note par son identifiant.
     *
     * @param id identifiant unique de la note
     * @return 200 avec la note si trouvée, sinon 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable String id) {
        return noteService.getNoteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    /**
     * Crée une nouvelle note.
     * <p>
     * <b>Important :</b> ne pas setter <code>createdAt</code> côté contrôleur ;
     * laisser l’auditing (ex. {@code @CreatedDate}) le gérer côté persistance.
     * </p>
     *
     * @param note note à créer (contenu et patientId attendus)
     * @return la note persistée
     */
    @PostMapping
    public Note addNote(@RequestBody Note note) {
        //  NE PAS setter createdAt ici (auditing s'en occupe)
        //  note.setCreatedAt(LocalDateTime.now());  // À supprimer
        return noteService.saveNote(note);
    }
    /**
     * Met à jour le contenu d’une note existante.
     * <p>
     * Ne met à jour que le champ <code>content</code>. L’horodatage de modification
     * (ex. {@code @LastModifiedDate}) est géré par la couche persistance.
     * </p>
     *
     * @param id          identifiant de la note à modifier
     * @param updatedNote payload contenant au moins le nouveau contenu
     * @return 200 avec la note mise à jour si trouvée, 404 sinon ; 400 si incohérence d’ID
     */
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
    /**
     * Supprime une note par son identifiant.
     *
     * @param id identifiant de la note à supprimer
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable String id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}
