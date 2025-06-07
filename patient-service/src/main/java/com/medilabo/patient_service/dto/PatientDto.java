package com.medilabo.patient_service.dto;

import lombok.*;

import java.time.LocalDate;



/**
 * DTO représentant les informations d'un patient.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDto {

    private String prenom;
    private String nom;
    private LocalDate dateDeNaisssance;
    private String genre;
    private String adresse;
    private String telephone;
}
