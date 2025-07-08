package com.medilabo.diabetes_risk_service.service;

import com.medilabo.diabetes_risk_service.client.PatientClient;
import com.medilabo.diabetes_risk_service.client.NoteClient;
import com.medilabo.diabetes_risk_service.model.PatientDto;
import com.medilabo.diabetes_risk_service.model.NoteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiabetesRiskService {

    @Autowired
    private PatientClient patientClient;
    @Autowired
    private NoteClient noteClient;

    // Liste de mots-clés à rechercher dans les notes médicales
    private static final List<String> TRIGGERS = Arrays.asList(
            "hemoglobin", "microalbumin", "body height", "body weight", "smoker",
            "abnormal", "cholesterol", "dizziness", "relapse", "reaction",
            "antibodies"
    );

    /**
     * Calcule le niveau de risque de diabète pour un patient donné.
     * @param patientId ID du patient
     * @return Niveau de risque ("None", "Borderline", "In Danger", "Early Onset")
     */
    public String calculerRisque(Long patientId) {
        // 1. Récupérer le patient et ses notes
        PatientDto patient = patientClient.getPatientById(patientId);
        List<NoteDto> notes = noteClient.getNotesByPatientId(patientId.intValue());

        // 2. Compter le nombre de déclencheurs (mots-clés) dans les notes
        int triggerCount = 0;
        for (NoteDto note : notes) {
            String content = note.getContent() == null ? "" : note.getContent().toLowerCase();
            for (String trigger : TRIGGERS) {
                if (content.contains(trigger)) {
                    triggerCount++;
                }
            }
        }

        // 3. Calculer l'âge du patient
        int age = 0;
        if (patient.getDateDeNaissance() != null) {
            age = Period.between(patient.getDateDeNaissance(), LocalDate.now()).getYears();
        }

        String gender = patient.getGenre() == null ? "" : patient.getGenre().toUpperCase();

        // 4. Appliquer les règles du sujet
        // Exemple de logique à adapter selon ton énoncé
        if (triggerCount == 0) return "None";
        if (age > 30) {
            if (triggerCount >= 2 && triggerCount < 6) return "Borderline";
            if (triggerCount >= 6 && triggerCount < 8) return "In Danger";
            if (triggerCount >= 8) return "Early Onset";
        } else {
            if ("M".equals(gender)) {
                if (triggerCount >= 3 && triggerCount < 5) return "In Danger";
                if (triggerCount >= 5) return "Early Onset";
            } else if ("F".equals(gender)) {
                if (triggerCount >= 4 && triggerCount < 7) return "In Danger";
                if (triggerCount >= 7) return "Early Onset";
            }
        }
        return "None";
    }
}