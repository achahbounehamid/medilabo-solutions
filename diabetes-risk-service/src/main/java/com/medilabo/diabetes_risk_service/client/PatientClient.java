package com.medilabo.diabetes_risk_service.client;

import com.medilabo.diabetes_risk_service.model.PatientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-service", url = "${patient.service.url}")
public interface PatientClient {
    @GetMapping("/api/patients/{id}")
    PatientDto getPatientById(@PathVariable("id") Long id);
}
