package com.medilabo.patient_service.service;

import com.medilabo.patient_service.model.Patient;
import com.medilabo.patient_service.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
/**
 * Service applicatif pour la gestion des patients.
 *
 * <p>
 * Cette couche orchestre la logique métier et délègue l'accès aux données
 * au {@link PatientRepository}. Elle expose les opérations CRUD ainsi qu'une
 * recherche multi-critères (nom, prénom, date de naissance).
 * </p>
 */
@Service
public class PatientService {
    /** Couche d'accès aux données JPA pour {@link Patient}. */
    private final PatientRepository patientRepository;

    /**
     * Injection du {@link PatientRepository}.
     *
     * @param patientRepository repository JPA des patients
     */
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }


    /**
     * Crée un nouveau patient.
     * <p>Le champ {@code id} est forcé à {@code null} pour éviter toute collision d'identifiant.</p>
     *
     * @param patient entité patient à persister
     * @return le patient sauvegardé (avec identifiant généré)
     */
    public Patient save(Patient patient) {
        patient.setId(null); // éviter qu'un id soit imposé
        return patientRepository.save(patient);
    }

    /**
     * Retourne la liste complète des patients.
     *
     * @return liste de tous les patients
     */
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    /**
     * Recherche un patient par son identifiant.
     *
     * @param id identifiant du patient
     * @return un {@link Optional} contenant le patient s'il existe, sinon vide
     */
    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    /**
     * Met à jour un patient existant.
     *
     * @param id      identifiant du patient à mettre à jour
     * @param updated entité contenant les nouvelles valeurs
     * @return le patient mis à jour
     * @throws EntityNotFoundException si aucun patient n'existe avec l'id fourni
     */
    public Patient update(Long id, Patient updated) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient " + id + " introuvable"));

        existing.setPrenom(updated.getPrenom());
        existing.setNom(updated.getNom());
        existing.setDateDeNaissance(updated.getDateDeNaissance());
        existing.setGenre(updated.getGenre());
        existing.setAdresse(updated.getAdresse());
        existing.setTelephone(updated.getTelephone());

        return patientRepository.save(existing);
    }

    /**
     * Supprime un patient par son identifiant.
     *
     * @param id identifiant du patient à supprimer
     */
    public void deleteById(Long id) {
        patientRepository.deleteById(id);
    }

    /**
     * Recherche de patients par critères optionnels.
     * <p>
     * Les combinaisons suivantes sont gérées (par ordre de priorité) :
     * </p>
     * <ol>
     *   <li>{@code nom + prenom + dateDeNaissance}</li>
     *   <li>{@code nom + prenom}</li>
     *   <li>{@code nom} seul</li>
     *   <li>{@code prenom} seul</li>
     *   <li>{@code dateDeNaissance} seule</li>
     *   <li>aucun critère ⇒ retourne {@link #findAll()}</li>
     * </ol>
     *
     * @param nom             nom (optionnel)
     * @param prenom          prénom (optionnel)
     * @param dateDeNaissance date de naissance (optionnelle)
     * @return liste des patients correspondant aux critères (possiblement vide)
     */
    public List<Patient> search(String nom, String prenom, LocalDate dateDeNaissance) {
        if (nom != null && !nom.isBlank()
                && prenom != null && !prenom.isBlank()
                && dateDeNaissance != null) {
            return patientRepository.findByNomIgnoreCaseAndPrenomIgnoreCaseAndDateDeNaissance(nom, prenom, dateDeNaissance);
        }
        if (nom != null && !nom.isBlank()
                && prenom != null && !prenom.isBlank()) {
            return patientRepository.findByNomIgnoreCaseAndPrenomIgnoreCase(nom, prenom);
        }
        if (nom != null && !nom.isBlank()) {
            return patientRepository.findByNomIgnoreCase(nom);
        }
        if (prenom != null && !prenom.isBlank()) {
            return patientRepository.findByPrenomIgnoreCase(prenom);
        }
        if (dateDeNaissance != null) {
            return patientRepository.findByDateDeNaissance(dateDeNaissance);
        }
        return patientRepository.findAll();
    }

}

