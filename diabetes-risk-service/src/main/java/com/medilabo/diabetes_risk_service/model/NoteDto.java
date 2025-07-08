package com.medilabo.diabetes_risk_service.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteDto {
    private Integer patientId;
    private String content;
}