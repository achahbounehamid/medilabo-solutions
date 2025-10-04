package com.medilabo.patient_service.controller;

import com.medilabo.patient_service.model.Patient;
import com.medilabo.patient_service.service.PatientService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/patients")
public class PatientController {
    /** Service applicatif gérant la logique métier et l'accès aux données Patient. */
    private final PatientService patientService;

    /**
     * Injection du {@link PatientService}.
     *
     * @param patientService service métier des patients
     */
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Récupère la liste complète des patients.
     *
     * <p>GET {@code /api/patients}</p>
     *
     * @return liste de tous les patients
     */
    @GetMapping
    public List<Patient> getUsers() {
        return patientService.findAll();
    }
    /**
     * Récupère un patient par son identifiant.
     *
     * <p>GET {@code /api/patients/{id}}</p>
     *
     * @param id identifiant du patient
     * @return 200 avec le patient si trouvé, sinon 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> patient = patientService.findById(id);
        return patient.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    /**
     * Crée un nouveau patient.
     *
     * <p>POST {@code /api/patients}</p>
     *
     * <p>Retourne 201 Created avec l'en-tête {@code Location} pointant vers
     * {@code /api/patients/{id}}.</p>
     *
     * @param patient payload du patient à créer (validé)
     * @return 201 Created + corps du patient créé
     */
    @PostMapping
    public ResponseEntity<Patient> addPatient(@RequestBody @Valid Patient patient) {
        Patient saved = patientService.save(patient); // ou create(...)
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(location).body(saved); // 201 Created
    }

    /**
     * Met à jour un patient existant.
     *
     * <p>PUT {@code /api/patients/{id}}</p>
     *
     * @param id      identifiant du patient à mettre à jour
     * @param updated payload contenant les nouvelles valeurs (validé)
     * @return 200 avec le patient mis à jour, ou 404 si introuvable
     */
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id,
                                                 @RequestBody @Valid Patient updated) {
        try {
            return ResponseEntity.ok(patientService.update(id, updated));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Recherche de patients selon des critères (tous optionnels).
     *
     * <p>GET {@code /api/patients/search?nom=&prenom=&dateDeNaissance=YYYY-MM-DD}</p>
     *
     * @param nom              nom (optionnel)
     * @param prenom           prénom (optionnel)
     * @param dateDeNaissance  date de naissance (ISO, optionnelle)
     * @return liste des patients correspondant aux critères (peut être vide)
     */
    //  Aligner les noms avec le modèle/repository: nom, prenom, dateDeNaissance
    @GetMapping("/search")
    public List<Patient> searchPatients(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDeNaissance
    ) {
        return patientService.search(nom, prenom, dateDeNaissance);
    }

    /**
     * Supprime un patient par son identifiant.
     *
     * <p>DELETE {@code /api/patients/{id}}</p>
     *
     * @param id identifiant du patient
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
