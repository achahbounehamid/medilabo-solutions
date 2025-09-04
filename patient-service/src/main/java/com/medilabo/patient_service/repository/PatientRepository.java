package com.medilabo.patient_service.repository;

import com.medilabo.patient_service.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByNomIgnoreCase(String nom);
    List<Patient> findByPrenomIgnoreCase(String prenom);
    List<Patient> findByDateDeNaissance(LocalDate dateDeNaissance);
    List<Patient> findByNomIgnoreCaseAndPrenomIgnoreCase(String nom, String prenom);
    List<Patient> findByNomIgnoreCaseAndPrenomIgnoreCaseAndDateDeNaissance(String nom, String prenom, LocalDate dateDeNaissance);
}

