//package com.medilabo.front.controller;
//import com.medilabo.front.model.Patient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//import jakarta.validation.Valid;
//
//
//import java.time.LocalDate;
//import java.util.Arrays;
//
//
//@Controller
//public class FrontController {
//
//    private final RestTemplate restTemplate;
//
//    public FrontController(RestTemplate restTemplate) { this.restTemplate = restTemplate; }
//
//    @Value("${patient.service.url}")
//    private String patientServiceUrl;
//
//    @Value("${risk.service.url}")
//    private String riskServiceUrl;
//
//    @GetMapping("/homePage")
//    public String homePage(Model model) {
//        ResponseEntity<Patient[]> response =
//                restTemplate.getForEntity(patientServiceUrl + "/api/patients", Patient[].class);
//        model.addAttribute("patients", Arrays.asList(response.getBody()));
//        return "homePage";
//    }
//
//    @GetMapping("/login")
//    public String login() { return "login"; }
//
//    @GetMapping("/patients")
//    public String patientList() { return "patientInfoPage"; }
//
//    @GetMapping("/patients/add")
//    public String addPatient(Model model) {
//        model.addAttribute("patient", new Patient());
//        return "addPatientPage";
//    }
//
//
//    @PostMapping("/patients/add")
//    public String savePatient(@Valid @ModelAttribute Patient patient,
//                              BindingResult errors, Model model) {
//        if (errors.hasErrors()) {
//            return "addPatientPage"; // réaffiche le formulaire avec les messages
//        }
//        restTemplate.postForEntity(patientServiceUrl + "/api/patients", patient, Patient.class);
//        return "redirect:/homePage";
//    }
//
//
//    @GetMapping("/patient/update/{id}")
//    public String showUpdateForm(@PathVariable Long id, Model model) {
//        ResponseEntity<Patient> response =
//                restTemplate.getForEntity(patientServiceUrl + "/api/patients/" + id, Patient.class);
//        model.addAttribute("patient", response.getBody());
//        return "updatePatientPage";
//    }
//
//    @PostMapping("/patient/update/{id}")
//    public String updatePatient(@PathVariable Long id, @ModelAttribute Patient patient) {
//        restTemplate.put(patientServiceUrl + "/api/patients/" + id, patient);
//        return "redirect:/homePage";
//    }
//
//
//
//    @GetMapping("/patient/delete/{id}")
//    public String deletePatient(@PathVariable Long id) {
//        restTemplate.delete(patientServiceUrl + "/api/patients/" + id);
//        return "redirect:/homePage";
//    }
//
//    @GetMapping("/patients/search")
//    public String searchPatients(
//            @RequestParam(required = false) String lastName,
//            @RequestParam(required = false) String firstName,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
//            Model model
//    ) {
//        //  le patient-service attend nom/prenom/dateDeNaissance
//        UriComponentsBuilder builder = UriComponentsBuilder
//                .fromHttpUrl(patientServiceUrl + "/api/patients/search");
//
//        if (lastName != null && !lastName.isBlank())  builder.queryParam("nom", lastName);
//        if (firstName != null && !firstName.isBlank()) builder.queryParam("prenom", firstName);
//        if (dateOfBirth != null)                       builder.queryParam("dateDeNaissance", dateOfBirth);
//
//        ResponseEntity<Patient[]> response =
//                restTemplate.getForEntity(builder.toUriString(), Patient[].class);
//
//        model.addAttribute("patients", Arrays.asList(response.getBody()));
//        return "patientInfoPage";
//    }
//
//    @GetMapping("/patient/infos/{id}")
//    public String afficherFichePatient(@PathVariable Long id, Model model) {
//        // Patient
//        Patient patient = restTemplate.getForObject(
//                patientServiceUrl + "/api/patients/{id}", Patient.class, id);
//        model.addAttribute("patient", patient);
//
//        // Risk (via propriété)
//        String risk = restTemplate.getForObject(
//                riskServiceUrl + "/api/diabetes-risk/{id}", String.class, id);
//        model.addAttribute("riskLevel", risk);
//
//        return "patientDetailsPage";
//    }
//}
package com.medilabo.front.controller;

import com.medilabo.front.model.Note;
import com.medilabo.front.model.Patient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FrontController {

    private final RestTemplate restTemplate;

    public FrontController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${patient.service.url}")
    private String patientServiceUrl;

    @Value("${note.service.url}")
    private String noteServiceUrl;

    @Value("${risk.service.url}")
    private String riskServiceUrl;

    /* -------- Accueil / Login -------- */

    // (Optionnel) redirige "/" vers l'accueil
    @GetMapping("/")
    public String root() {
        return "redirect:/homePage";
    }

    @GetMapping("/homePage")
    public String homePage(Model model) {
        ResponseEntity<Patient[]> response =
                restTemplate.getForEntity(patientServiceUrl + "/api/patients", Patient[].class);
        model.addAttribute("patients", Arrays.asList(response.getBody()));
        return "homePage";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /* -------- Patients: add / update / delete -------- */

    @GetMapping("/patients/add")
    public String addPatient(Model model) {
        model.addAttribute("patient", new Patient());
        return "addPatientPage";
    }

    @PostMapping("/patients/add")
    public String savePatient(@Valid @ModelAttribute Patient patient,
                              BindingResult errors) {
        if (errors.hasErrors()) {
            return "addPatientPage"; // réaffiche le formulaire avec messages
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

    /* -------- Recherche (rendue dans homePage) -------- */

    @GetMapping("/patients/search")
    public String searchPatients(@RequestParam(required = false) String lastName,
                                 @RequestParam(required = false) String firstName,
                                 @RequestParam(required = false)
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
                                 Model model) {

        var builder = UriComponentsBuilder
                .fromHttpUrl(patientServiceUrl + "/api/patients/search");
        if (lastName != null && !lastName.isBlank())  builder.queryParam("nom", lastName);
        if (firstName != null && !firstName.isBlank()) builder.queryParam("prenom", firstName);
        if (dateOfBirth != null)                       builder.queryParam("dateDeNaissance", dateOfBirth);

        ResponseEntity<Patient[]> response =
                restTemplate.getForEntity(builder.toUriString(), Patient[].class);
        Patient[] results = response.getBody();

        model.addAttribute("lastName", lastName);
        model.addAttribute("firstName", firstName);
        model.addAttribute("dateOfBirth", dateOfBirth);

        if (results == null || results.length == 0) {
            model.addAttribute("patients", List.of());
            model.addAttribute("errorMessage", "Aucun patient trouvé avec ces critères.");
            return "homePage";
        }

        if (results.length == 1 && results[0].getId() != null) {
            Long id = results[0].getId();
            try {
                restTemplate.getForEntity(patientServiceUrl + "/api/patients/" + id, Patient.class); // pré-check
                return "redirect:/patient/infos/" + id;
            } catch (Exception e) {
                model.addAttribute("patients", Arrays.asList(results));
                model.addAttribute("errorMessage", "Patient trouvé, mais la fiche (id=" + id + ") est introuvable.");
                return "homePage";
            }
        }

        model.addAttribute("patients", Arrays.asList(results));
        model.addAttribute("successMessage", results.length + " patients trouvés.");
        return "homePage";
    }



    /* -------- Fiche patient (infos + risque + notes + ajout note) -------- */

    @GetMapping("/patient/infos/{id}")
    public String afficherFichePatient(@PathVariable Long id,
                                       Model model,
                                       RedirectAttributes ra) {
        // 1) Patient
        Patient patient;
        try {
            patient = restTemplate.getForObject(
                    patientServiceUrl + "/api/patients/{id}", Patient.class, id);
            if (patient == null) {
                ra.addFlashAttribute("errorMessage", "Patient introuvable (id=" + id + ").");
                return "redirect:/homePage";
            }
        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            ra.addFlashAttribute("errorMessage", "Patient introuvable (id=" + id + ").");
            return "redirect:/homePage";
        } catch (org.springframework.web.client.RestClientException e) {
            ra.addFlashAttribute("errorMessage", "Service patient indisponible.");
            return "redirect:/homePage";
        }
        model.addAttribute("patient", patient);

        // 2) Risk (ne bloque pas l'affichage)
        String risk = "Unknown";
        try {
            String r = restTemplate.getForObject(
                    riskServiceUrl + "/api/diabetes-risk/{id}", String.class, id);
            if (r != null) risk = r;
        } catch (org.springframework.web.client.RestClientException ignored) {
            // on garde "Unknown"
        }
        model.addAttribute("riskLevel", risk);

        // 3) Notes (ne bloque pas l'affichage)
        List<Note> notes = java.util.Collections.emptyList();
        try {
            ResponseEntity<List<Note>> resp = restTemplate.exchange(
                    noteServiceUrl + "/api/notes/patient/{id}",
                    HttpMethod.GET,
                    null,
                    new org.springframework.core.ParameterizedTypeReference<List<Note>>() {},
                    id
            );
            if (resp.getBody() != null) notes = resp.getBody();
        } catch (org.springframework.web.client.RestClientException ignored) {
            // on garde la liste vide
        }
        model.addAttribute("notes", notes);

        return "patientDetailsPage";
    }


    @PostMapping("/patient/{patientId}/notes")
    public String addNote(@PathVariable Long patientId, @RequestParam String content) {
        Map<String, Object> body = new HashMap<>();
        body.put("patientId", patientId);
        body.put("content", content);
        restTemplate.postForEntity(noteServiceUrl + "/api/notes", body, Void.class);
        return "redirect:/patient/infos/" + patientId;
    }
}
