package com.medilabo.front.controller;
import com.medilabo.front.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import jakarta.validation.Valid;


import java.time.LocalDate;
import java.util.Arrays;


@Controller
public class FrontController {

    private final RestTemplate restTemplate;

    public FrontController(RestTemplate restTemplate) { this.restTemplate = restTemplate; }

    @Value("${patient.service.url}")
    private String patientServiceUrl;

    @Value("${risk.service.url}")
    private String riskServiceUrl;

    @GetMapping("/homePage")
    public String homePage(Model model) {
        ResponseEntity<Patient[]> response =
                restTemplate.getForEntity(patientServiceUrl + "/api/patients", Patient[].class);
        model.addAttribute("patients", Arrays.asList(response.getBody()));
        return "homePage";
    }

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/patients")
    public String patientList() { return "patientInfoPage"; }

    @GetMapping("/patients/add")
    public String addPatient(Model model) {
        model.addAttribute("patient", new Patient());
        return "addPatientPage";
    }


    @PostMapping("/patients/add")
    public String savePatient(@Valid @ModelAttribute Patient patient,
                              BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            return "addPatientPage"; // réaffiche le formulaire avec les messages
        }
        restTemplate.postForEntity(patientServiceUrl + "/api/patients", patient, Patient.class);
        return "redirect:/homePage";
    }


    @GetMapping("/patient/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        ResponseEntity<Patient> response =
                restTemplate.getForEntity(patientServiceUrl + "/api/patients/" + id, Patient.class);
        model.addAttribute("patient", response.getBody());
        return "updatePatientPage";
    }

    @PostMapping("/patient/update/{id}")
    public String updatePatient(@PathVariable Long id, @ModelAttribute Patient patient) {
        restTemplate.put(patientServiceUrl + "/api/patients/" + id, patient);
        return "redirect:/homePage";
    }



    @GetMapping("/patient/delete/{id}")
    public String deletePatient(@PathVariable Long id) {
        restTemplate.delete(patientServiceUrl + "/api/patients/" + id);
        return "redirect:/homePage";
    }

    @GetMapping("/patients/search")
    public String searchPatients(
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            Model model
    ) {
        //  le patient-service attend nom/prenom/dateDeNaissance
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(patientServiceUrl + "/api/patients/search");

        if (lastName != null && !lastName.isBlank())  builder.queryParam("nom", lastName);
        if (firstName != null && !firstName.isBlank()) builder.queryParam("prenom", firstName);
        if (dateOfBirth != null)                       builder.queryParam("dateDeNaissance", dateOfBirth);

        ResponseEntity<Patient[]> response =
                restTemplate.getForEntity(builder.toUriString(), Patient[].class);

        model.addAttribute("patients", Arrays.asList(response.getBody()));
        return "patientInfoPage";
    }

    @GetMapping("/patient/infos/{id}")
    public String afficherFichePatient(@PathVariable Long id, Model model) {
        // Patient
        Patient patient = restTemplate.getForObject(
                patientServiceUrl + "/api/patients/{id}", Patient.class, id);
        model.addAttribute("patient", patient);

        // Risk (via propriété)
        String risk = restTemplate.getForObject(
                riskServiceUrl + "/api/diabetes-risk/{id}", String.class, id);
        model.addAttribute("riskLevel", risk);

        return "patientDetailsPage";
    }
}
