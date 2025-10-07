package com.note_service.service;

import com.note_service.model.Note;
import com.note_service.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    /** Couche d'accès aux données pour l'entité {@link Note}. */
    @Autowired
    private NoteRepository noteRepository;

    /**
     * Récupère l'ensemble des notes.
     *
     * @return la liste complète des notes persistées
     */
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    /**
     * Récupère les notes d'un patient, triées par date de création décroissante
     * (les plus récentes en premier).
     *
     * @param patientId identifiant du patient
     * @return liste des notes du patient, triée
     */
    public List<Note> getNotesByPatientIdDesc(Integer patientId) {
        return noteRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    /**
     * Recherche une note par son identifiant.
     *
     * @param id identifiant unique de la note
     * @return un {@code Optional} contenant la note si trouvée, sinon vide
     */
    public Optional<Note> getNoteById(String id) {
        return noteRepository.findById(id);
    }

    /**
     * Crée ou met à jour une note.
     * <p>
     * Les horodatages (ex. {@code createdAt}, {@code updatedAt}) sont gérés par
     * l'auditing Spring Data s'il est activé.
     * </p>
     *
     * @param note la note à persister
     * @return la note persistée (potentiellement enrichie par la couche de persistance)
     */
    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }

    /**
     * Supprime une note par son identifiant.
     *
     * @param id identifiant de la note à supprimer
     */
    public void deleteNote(String id) {
        noteRepository.deleteById(id);
    }
}
