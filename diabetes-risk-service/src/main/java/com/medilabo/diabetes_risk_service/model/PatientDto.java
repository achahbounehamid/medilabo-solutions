package com.medilabo.diabetes_risk_service.model;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDto {
    private Long id;
    private String prenom;
    private String nom;
    private LocalDate dateDeNaissance;
    private String genre;
    private String adresse;
    private String telephone;
}
