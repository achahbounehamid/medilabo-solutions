package com.medilabo.diabetes_risk_service.controller;


import com.medilabo.diabetes_risk_service.service.DiabetesRiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/diabetes-risk")
public class DiabetesRiskController {
    @Autowired
    private DiabetesRiskService diabetesRiskService;

    @GetMapping("/{patientId}")
    public ResponseEntity<String> getRisk(@PathVariable Long patientId) {
        String risk = diabetesRiskService.calculerRisque(patientId);
        return ResponseEntity.ok(risk);
    }
}
