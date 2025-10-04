package com.medilabo.diabetes_risk_service.client;


import com.medilabo.diabetes_risk_service.model.NoteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

/**
 * DTO représentant un patient tel qu'exposé par le patient-service.
 */

@FeignClient(name = "note-service", url = "${note.service.url}")
public interface NoteClient {

    /**
     * Récupère la liste des notes médicales pour un patient donné.
     *
     * @param patientId identifiant fonctionnel du patient (non nul)
     * @return liste des notes du patient (liste vide si aucune note)
     * @throws org.springframework.web.server.ResponseStatusException si l'API distante renvoie une erreur HTTP
     */
    @GetMapping("/api/notes/patient/{patientId}")
    List<NoteDto> getNotesByPatientId(@PathVariable("patientId") Integer patientId);
}

