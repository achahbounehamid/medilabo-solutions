package com.medilabo.diabetes_risk_service.controller;


import com.medilabo.diabetes_risk_service.service.DiabetesRiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Contrôleur REST exposant l'API d'évaluation du risque de diabète.
 *
 * <p>Microservice : <b>diabetes-risk-service</b></p>
 *
 * <h2>Endpoints</h2>
 * <ul>
 *   <li><b>GET /api/diabetes-risk/{patientId}</b> — Calcule et retourne le niveau de risque pour le patient.</li>
 * </ul>
 *
 * <h2>Comportement</h2>
 * <ul>
 *   <li>Délègue le calcul au {@link DiabetesRiskService}.</li>
 *   <li>Retourne une réponse HTTP 200 avec une chaîne représentant le niveau de risque.</li>
 * </ul>
 *
 * <h2>Erreurs possibles (à gérer via un {@code @ControllerAdvice})</h2>
 * <ul>
 *   <li>404 si le patient n'existe pas (ex. exception métier PatientNotFoundException).</li>
 *   <li>502/503 si un service distant requis est indisponible (clients Feign).</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/diabetes-risk")
public class DiabetesRiskController {
    @Autowired
    private DiabetesRiskService diabetesRiskService;
    /**
     * Calcule le risque de diabète pour un patient donné.
     *
     * @param patientId identifiant du patient
     * @return 200 OK avec le niveau de risque sous forme de chaîne (ex. "None", "Borderline", "In Danger", "Early onset")
     */
    @GetMapping("/{patientId}")
    public ResponseEntity<String> getRisk(@PathVariable Long patientId) {
        String risk = diabetesRiskService.calculerRisque(patientId);
        return ResponseEntity.ok(risk);
    }
}
