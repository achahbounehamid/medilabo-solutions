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

    @Autowired private PatientClient patientClient;
    @Autowired private NoteClient noteClient;

    // Triggers EN + FR (ajuste/complète si besoin)
    private static final List<String> TRIGGERS = Arrays.asList(
            // EN
            "hemoglobin a1c", "hemoglobin", "microalbumin", "body height", "height",
            "body weight", "weight", "smoker", "abnormal", "cholesterol", "dizziness",
            "relapse", "reaction", "antibodies",
            // FR équivalents courants
            "hemoglobine a1c", "hemoglobine", "microalbumine", "taille", "poids",
            "fumeur", "anormal", "cholesterol", "etourdissement", "etourdissements",
            "rechute", "reaction", "anticorps",
            // Termes FR souvent présents dans tes notes
            "soif excessive", "fatigue persistante", "hyperglycemie", "perte de poids"
    );

    /** Normalise : minuscules + suppression des accents. */
    private static String normalize(String s) {
        if (s == null) return "";
        String n = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", ""); // enlève les diacritiques
        return n.toLowerCase(java.util.Locale.ROOT);
    }

    /**
     * Calcule le niveau de risque de diabète pour un patient donné.
     * @param patientId ID du patient
     * @return "None", "Borderline", "In Danger", "Early Onset"
     */
    public String calculerRisque(Long patientId) {
        // 1) Patient + notes
        PatientDto patient = patientClient.getPatientById(patientId);
        List<NoteDto> notes = noteClient.getNotesByPatientId(patientId.intValue());

        // 2) Comptage des triggers sur l’ensemble des notes
        int triggerCount = 0;
        for (NoteDto note : notes) {
            String content = normalize(note.getContent());
            // Option : on ne compte chaque trigger qu’une fois par note
            boolean[] seen = new boolean[TRIGGERS.size()];
            for (int i = 0; i < TRIGGERS.size(); i++) {
                String trig = TRIGGERS.get(i);
                if (!seen[i] && content.contains(trig)) {
                    triggerCount++;
                    seen[i] = true;
                }
            }
        }

        // 3) Âge / genre
        int age = 0;
        if (patient.getDateDeNaissance() != null) {
            age = java.time.Period.between(patient.getDateDeNaissance(), java.time.LocalDate.now()).getYears();
        }
        String gender = patient.getGenre() == null ? "" : patient.getGenre().toUpperCase();

        // 4) Règles (tes règles actuelles conservées)
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
