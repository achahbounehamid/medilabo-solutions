package com.medilabo.patientservice.controller;

import com.medilabo.patientservice.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.medilabo.patientservice.service.PatientService;


import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")

public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping
    public List<Patient> getUsers() {

        return patientService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> patient = patientService.findById(id);
        return patient.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Patient> addPatient(@RequestBody @Valid Patient Long, @RequestBody @Valid Patient patient) {

        return ResponseEntity.ok(patientService.save(patient));
    }

    @PutMapping("/{id}")
    public  ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody @Valid Patient patient){
        patient.setId(id);
        return ResponseEntity.ok(patientService.save(patient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable  Long id) {
        patientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
