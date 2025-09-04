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

@Service
public class PatientService {


//    @Autowired
//    private PatientRepository patientRepository;
//
//
//
//    // Ajouter un utilisateur
//    public Patient save(Patient patient) {
//        return patientRepository.save(patient);
//    }
//
//    // Lister tous les utilisateurs
//    public List<Patient> findAll() {
//
//        return patientRepository.findAll();
//    }
//    public List<Patient> search(String lastName, String firstName, LocalDate dateOfBirth) {
//        if (lastName != null && !lastName.isEmpty()) {
//            return patientRepository.findByNomIgnoreCase(lastName);
//        }
//        // Ajoute d'autres cas combinés si besoin
//        return patientRepository.findAll(); // par défaut, retourne tout
//    }
//
//
//    // Trouver un utilisateur par son ID
//    public Optional<Patient> findById(Long id) {
//
//        return patientRepository.findById(id);
//    }
//
//    // Supprimer un utilisateur
//    public void deleteById(Long id) {
//
//        patientRepository.deleteById(id);
//    }

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // CREATE
    public Patient save(Patient patient) {
        patient.setId(null); // éviter qu'un id soit imposé
        return patientRepository.save(patient);
    }

    // READ
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    // UPDATE
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

    // DELETE
    public void deleteById(Long id) {
        patientRepository.deleteById(id);
    }

    // SEARCH
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

