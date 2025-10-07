package com.note_service.repository;

import com.note_service.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
/**
 * Repository Spring Data MongoDB pour l'entité {@link Note}.
 *
 * <p>
 * Hérite des opérations CRUD standards via {@link MongoRepository}.
 * Cette interface déclare en plus une méthode de requête dérivée pour
 * récupérer les notes d'un patient triées par date de création décroissante.
 * </p>
 */
public interface NoteRepository extends MongoRepository<Note, String> {
    /**
     * Retourne les notes d'un patient triées par date de création décroissante.
     *
     * @param patientId identifiant du patient
     * @return liste des notes associées, triée (les plus récentes d'abord)
     */
    List<Note> findByPatientIdOrderByCreatedAtDesc(Integer patientId);
}