package com.medilabo.diabetes_risk_service.client;

import com.medilabo.diabetes_risk_service.model.PatientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
/**
 * Client Feign pour consommer l'API du microservice <b>patient-service</b>.
 * <p>
 * L'URL du service est injectée via la propriété {@code patient.service.url}.
 * Ce client permet de récupérer les informations d'un patient par son identifiant.
 * </p>
 *
 * <h2>Configuration</h2>
 * <ul>
 *   <li>Propriété requise : {@code patient.service.url} (ex. {@code http://localhost:8081})</li>
 *   <li>Endpoint consommé : {@code GET /api/patients/{id}}</li>
 * </ul>
 *
 * <h2>Responsabilités</h2>
 * <ul>
 *   <li>Appeler l'endpoint distant pour obtenir un {@link PatientDto}.</li>
 *   <li>Mapper la réponse JSON vers le DTO.</li>
 * </ul>
 *
 * <h2>Remarques</h2>
 * <ul>
 *   <li>En cas de 404, l’implémentation Feign lève une exception (à gérer côté appelant).</li>
 *   <li>Il est recommandé de configurer des timeouts et/ou un fallback.</li>
 * </ul>
 */
@FeignClient(name = "patient-service", url = "${patient.service.url}")
public interface PatientClient {
    /**
     * Récupère les informations d'un patient par identifiant.
     *
     * @param id identifiant technique du patient (non nul)
     * @return le patient correspondant
     * @throws feign.FeignException.NotFound si aucun patient n'est trouvé
     * @throws feign.FeignException en cas d'erreur HTTP distante
     */
    @GetMapping("/api/patients/{id}")
    PatientDto getPatientById(@PathVariable("id") Long id);
}
