package com.medilabo.patient_service.controller;

import com.medilabo.patient_service.model.Patient;
import com.medilabo.patient_service.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
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
    public ResponseEntity<Patient> addPatient(@RequestBody @Valid Patient patient) {

        return ResponseEntity.ok(patientService.save(patient));
    }
    @GetMapping("/search")
    public List<Patient> searchPatients(
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth
    ) {
        return patientService.search(lastName, firstName, dateOfBirth);
    }

//    @PutMapping("/{id}")
//    public  ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody @Valid Patient patient){
//        patient.setId(id);
//        return ResponseEntity.ok(patientService.save(patient));
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable  Long id) {
        patientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
