package com.medilabo.patientservice.service;

import com.medilabo.patientservice.model.Patient;
import com.medilabo.patientservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {


        @Autowired
        private PatientRepository patientRepository;



        // Ajouter un utilisateur
        public Patient save(Patient patient) {
            return patientRepository.save(patient);
        }

        // Lister tous les utilisateurs
        public List<Patient> findAll() {

            return patientRepository.findAll();
        }

        // Trouver un utilisateur par son ID
        public Optional<Patient> findById(Long id) {

            return patientRepository.findById(id);
        }

        // Supprimer un utilisateur
        public void deleteById(Long id) {

            patientRepository.deleteById(id);
        }

}
