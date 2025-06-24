package com.medilabo.patient_service.repository;

import com.medilabo.patient_service.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository  extends JpaRepository<Patient, Long> {
    List<Patient> findByNomIgnoreCase(String nom);

}
