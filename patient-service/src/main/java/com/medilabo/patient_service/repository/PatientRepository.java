package com.medilabo.patient_service.repository;

import com.medilabo.patient_service.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
/**
 * Repository Spring Data JPA pour l'entité {@link Patient}.
 *
 * <p>
 * Fournit les opérations CRUD standard via {@link JpaRepository} et
 * expose des méthodes de requêtes dérivées pour rechercher des patients
 * par nom, prénom et/ou date de naissance.
 * </p>
 */
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Recherche les patients par nom (insensible à la casse).
     *
     * @param nom nom recherché
     * @return liste des patients correspondant
     */
    List<Patient> findByNomIgnoreCase(String nom);
    /**
     * Recherche les patients par prénom (insensible à la casse).
     *
     * @param prenom prénom recherché
     * @return liste des patients correspondant
     */
    List<Patient> findByPrenomIgnoreCase(String prenom);
    /**
     * Recherche les patients par date de naissance exacte.
     *
     * @param dateDeNaissance date de naissance (format {@link LocalDate})
     * @return liste des patients correspondant
     */
    List<Patient> findByDateDeNaissance(LocalDate dateDeNaissance);
    /**
     * Recherche les patients par nom et prénom (insensible à la casse).
     *
     * @param nom    nom recherché
     * @param prenom prénom recherché
     * @return liste des patients correspondant
     */
    List<Patient> findByNomIgnoreCaseAndPrenomIgnoreCase(String nom, String prenom);
    /**
     * Recherche les patients par nom, prénom (insensibles à la casse) et date de naissance.
     *
     * @param nom              nom recherché
     * @param prenom           prénom recherché
     * @param dateDeNaissance  date de naissance recherchée
     * @return liste des patients correspondant
     */
    List<Patient> findByNomIgnoreCaseAndPrenomIgnoreCaseAndDateDeNaissance(String nom, String prenom, LocalDate dateDeNaissance);
}

