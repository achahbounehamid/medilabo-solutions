package com.medilabo.patient_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
/**
 * Entité JPA représentant un patient.
 *
 * <p>
 * Mappée sur la table <b>patient</b>. Les contraintes de validation portent notamment
 * sur les champs nom, prénom et téléphone. Le champ <code>dateDeNaissance</code> est
 * requis au niveau base (nullable = false).
 * </p>
 */
@Entity
@Table(name = "patient")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String prenom;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String nom;

    @Column(name = "date_de_naissance", nullable = false)
    private java.time.LocalDate dateDeNaissance;

    @Column(length = 1)
    private String genre; // ex: "M" ou "F"

    @Column(length = 255)
    private String adresse;

    @NotBlank
    @Pattern(regexp = "^\\d{10,15}$", message = "Téléphone : uniquement des chiffres (10 à 15).")
    @Column(length = 15, nullable = false)
    private String telephone;
}


