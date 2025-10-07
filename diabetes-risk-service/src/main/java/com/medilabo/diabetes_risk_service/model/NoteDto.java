package com.medilabo.diabetes_risk_service.model;

import lombok.*;
/**
 * DTO représentant une note médicale associée à un patient.

 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteDto {
    private Integer patientId;
    private String content;
}