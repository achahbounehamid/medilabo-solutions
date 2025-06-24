package com.medilabo.front.controller;
import com.medilabo.front.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;


@Controller
public class FrontController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${patient.service.url}")
    private String patientServiceUrl;

    @GetMapping("/homePage")
    public String homePage(Model model) {

        ResponseEntity<Patient[]> response = restTemplate.getForEntity(patientServiceUrl + "/api/patients", Patient[].class);
        Patient[] patients = response.getBody();
        model.addAttribute("patients", Arrays.asList(patients));
        return "homePage";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }



    @GetMapping("/patients")
    public String patientList() {
        return "patientInfoPage";
    }

    @GetMapping("/patients/add")
    public String addPatient() {
        return "addPatientPage";
    }

    @PostMapping("/patients/add")
    public String savePatient(@ModelAttribute Patient patient) {
        restTemplate.postForEntity(patientServiceUrl + "/api/patients", patient, Patient.class);
        return "redirect:/homePage";
    }

    @GetMapping("/patient/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        // Appel au backend pour récupérer les données du patient à modifier
        ResponseEntity<Patient> response = restTemplate.getForEntity(patientServiceUrl + "/api/patients/" + id, Patient.class);
        model.addAttribute("patient", response.getBody());
        return "updatePatientPage";// correspond à updatePatientPage.html
    }

    @PostMapping("/patient/update/{id}")
    public String updatePatient(@PathVariable Long id, @ModelAttribute Patient patient) {
        // Envoi des données modifiées au backend
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
        // Construire l’URL de recherche vers patient-service
        StringBuilder url = new StringBuilder(patientServiceUrl + "/api/patients/search?");

        if (lastName != null && !lastName.isEmpty()) {
            url.append("lastName=").append(lastName).append("&");
        }
        if (firstName != null && !firstName.isEmpty()) {
            url.append("firstName=").append(firstName).append("&");
        }
        if (dateOfBirth != null) {
            url.append("dateOfBirth=").append(dateOfBirth);
        }

        ResponseEntity<Patient[]> response = restTemplate.getForEntity(url.toString(), Patient[].class);
        Patient[] patients = response.getBody();

        model.addAttribute("patients", Arrays.asList(patients));
        return "patientInfoPage"; // ou "homePage" si tu veux afficher les résultats là
    }

}
