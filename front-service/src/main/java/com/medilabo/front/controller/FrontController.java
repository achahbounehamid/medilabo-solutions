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
//
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
/**
 * Contrôleur MVC (Thymeleaf) du front-service.
 *
 * <p>
 * Rôle : orchestrer les vues de l’interface (accueil, fiche patient, formulaires),
 * en consommant les APIs des microservices <b>patient-service</b>, <b>note-service</b>
 * et <b>diabetes-risk-service</b> via {@link RestTemplate}.
 * </p>
 *
 * <p>
 * Vues utilisées : <code>homePage</code>, <code>login</code>,
 * <code>addPatientPage</code>, <code>updatePatientPage</code>, <code>patientDetailsPage</code>.
 * </p>
 */
@Controller
public class FrontController {
    /** Client HTTP synchrone pour appeler les microservices. */
    private final RestTemplate restTemplate;

    public FrontController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /** URL de base du patient-service (ex: http://localhost:8081). */
    @Value("${patient.service.url}")
    private String patientServiceUrl;
    /** URL de base du patient-service (ex: http://localhost:8081). */
    @Value("${note.service.url}")
    private String noteServiceUrl;
    /** URL de base du diabetes-risk-service (ex: http://localhost:8083). */
    @Value("${risk.service.url}")
    private String riskServiceUrl;

    /* -------- Accueil / Login -------- */

    /**
     * Redirection de la racine de l’appli vers la page d’accueil.
     *
     * @return redirection vers <code>/homePage</code>
     */
    // (Optionnel) redirige "/" vers l'accueil
    @GetMapping("/")
    public String root() {
        return "redirect:/homePage";
    }

    /**
     * Page d’accueil listant les patients.
     * <p>Appelle <code>GET {patientServiceUrl}/api/patients</code>.</p>
     *
     * @param model modèle de vue pour exposer la liste <code>patients</code>
     * @return la vue <code>homePage</code>
     */
    @GetMapping("/homePage")
    public String homePage(Model model) {
        ResponseEntity<Patient[]> response =
                restTemplate.getForEntity(patientServiceUrl + "/api/patients", Patient[].class);
        model.addAttribute("patients", Arrays.asList(response.getBody()));
        return "homePage";
    }
    /**
     * Affiche la page de connexion.
     *
     * @return la vue <code>login</code>
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /* -------- Patients: add / update / delete -------- */

    /**
     * Affiche le formulaire d’ajout d’un patient.
     *
     * @param model modèle de vue (pré-rempli avec un {@link Patient} vide)
     * @return la vue <code>addPatientPage</code>
     */
    @GetMapping("/patients/add")
    public String addPatient(Model model) {
        model.addAttribute("patient", new Patient());
        return "addPatientPage";
    }
    /**
     * Soumet l’ajout d’un patient.
     * <p>Appelle <code>POST {patientServiceUrl}/api/patients</code>.</p>
     *
     * @param patient données saisies, validées par Bean Validation
     * @param errors  erreurs de validation, le cas échéant
     * @return redirection vers l’accueil en cas de succès, sinon ré-affiche le formulaire
     */
    @PostMapping("/patients/add")
    public String savePatient(@Valid @ModelAttribute Patient patient,
                              BindingResult errors) {
        if (errors.hasErrors()) {
            return "addPatientPage"; // réaffiche le formulaire avec messages
        }
        restTemplate.postForEntity(patientServiceUrl + "/api/patients", patient, Patient.class);
        return "redirect:/homePage";
    }
    /**
     * Affiche le formulaire de mise à jour pour un patient donné.
     * <p>Appelle <code>GET {patientServiceUrl}/api/patients/{id}</code>.</p>
     *
     * @param id    identifiant du patient
     * @param model modèle de vue (pré-rempli avec les données du patient)
     * @return la vue <code>updatePatientPage</code>
     */
    @GetMapping("/patient/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        ResponseEntity<Patient> response =
                restTemplate.getForEntity(patientServiceUrl + "/api/patients/" + id, Patient.class);
        model.addAttribute("patient", response.getBody());
        return "updatePatientPage";
    }
    /**
     * Soumet la mise à jour d’un patient.
     * <p>Appelle <code>PUT {patientServiceUrl}/api/patients/{id}</code>.</p>
     *
     * @param id      identifiant du patient
     * @param patient données mises à jour
     * @return redirection vers l’accueil
     */
    @PostMapping("/patient/update/{id}")
    public String updatePatient(@PathVariable Long id, @ModelAttribute Patient patient) {
        restTemplate.put(patientServiceUrl + "/api/patients/" + id, patient);
        return "redirect:/homePage";
    }
    /**
     * Supprime un patient.
     * <p>Appelle <code>DELETE {patientServiceUrl}/api/patients/{id}</code>.</p>
     *
     * @param id identifiant du patient
     * @return redirection vers l’accueil
     */
    @GetMapping("/patient/delete/{id}")
    public String deletePatient(@PathVariable Long id) {
        restTemplate.delete(patientServiceUrl + "/api/patients/" + id);
        return "redirect:/homePage";
    }

    /* -------- Recherche (rendue dans homePage) -------- */
    /**
     * Recherche des patients par nom/prénom/date de naissance.
     * <p>Appelle <code>GET {patientServiceUrl}/api/patients/search</code> avec query params.</p>
     *
     * @param lastName    nom (optionnel)
     * @param firstName   prénom (optionnel)
     * @param dateOfBirth date de naissance ISO (optionnelle)
     * @param model       modèle de vue pour réafficher critères et résultats
     * @param ra          attributs flash pour messages après redirection
     * @return <code>homePage</code> avec résultats, ou redirection vers la fiche si 1 seul résultat
     */

@GetMapping("/patients/search")
public String searchPatients(
        @RequestParam(required = false) String lastName,
        @RequestParam(required = false) String firstName,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
        Model model,
        RedirectAttributes ra) {

    // Construit l’URL vers le service patient
    UriComponentsBuilder builder = UriComponentsBuilder
            .fromHttpUrl(patientServiceUrl + "/api/patients/search");

    if (lastName != null && !lastName.isBlank()) {
        builder.queryParam("nom", lastName.trim());
    }
    if (firstName != null && !firstName.isBlank()) {
        builder.queryParam("prenom", firstName.trim());
    }
    if (dateOfBirth != null) {
        builder.queryParam("dateDeNaissance", dateOfBirth);
    }

    // garde les critères pour réafficher dans homePage
    model.addAttribute("lastName", lastName);
    model.addAttribute("firstName", firstName);
    model.addAttribute("dateOfBirth", dateOfBirth);

    Patient[] results;
    try {
        ResponseEntity<Patient[]> resp =
                restTemplate.getForEntity(builder.toUriString(), Patient[].class);
        results = resp.getBody();
    } catch (Exception ex) {
        model.addAttribute("patients", List.of());
        model.addAttribute("errorMessage", "Service patient indisponible.");
        return "homePage";
    }

    if (results == null || results.length == 0) {
        model.addAttribute("patients", List.of());
        model.addAttribute("errorMessage", "Aucun patient trouvé avec ces critères.");
        return "homePage";
    }

    // Un seul résultat → redirection automatique vers la fiche
    if (results.length == 1 && results[0] != null && results[0].getId() != null) {
        Long id = results[0].getId();
        return "redirect:/patient/infos/" + id; // DOIT correspondre à @GetMapping("/patient/infos/{id}")
    }

    // Plusieurs résultats → liste sur la page d’accueil
    model.addAttribute("patients", Arrays.asList(results));
    model.addAttribute("successMessage", results.length + " patients trouvés.");
    return "homePage";
}



    /* -------- Fiche patient (infos + risque + notes + ajout note) -------- */

    /**
     * Affiche la fiche d’un patient : infos d’identité, niveau de risque et notes.
     * <ul>
     *   <li>GET patient : <code>{patientServiceUrl}/api/patients/{id}</code></li>
     *   <li>GET risk    : <code>{riskServiceUrl}/api/diabetes-risk/{id}</code></li>
     *   <li>GET notes   : <code>{noteServiceUrl}/api/notes/patient/{id}</code></li>
     * </ul>
     * Les erreurs sur risk/notes n’empêchent pas l’affichage de la fiche.
     *
     * @param id    identifiant du patient
     * @param model modèle de vue (attributs : <code>patient</code>, <code>riskLevel</code>, <code>notes</code>)
     * @param ra    attributs flash pour messages en cas d’erreur bloquante (ex: patient introuvable)
     * @return la vue <code>patientDetailsPage</code>, ou redirection vers l’accueil en cas d’erreur
     */

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

    /**
     * Ajoute une note au patient et revient à la fiche.
     * <p>Appelle <code>POST {noteServiceUrl}/api/notes</code>.</p>
     *
     * @param patientId identifiant du patient
     * @param content   contenu de la note
     * @return redirection vers <code>/patient/infos/{patientId}</code>
     */
    @PostMapping("/patient/{patientId}/notes")
    public String addNote(@PathVariable Long patientId, @RequestParam String content) {
        Map<String, Object> body = new HashMap<>();
        body.put("patientId", patientId);
        body.put("content", content);
        restTemplate.postForEntity(noteServiceUrl + "/api/notes", body, Void.class);
        return "redirect:/patient/infos/" + patientId;
    }
}
