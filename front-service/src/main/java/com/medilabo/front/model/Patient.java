package com.medilabo.front.model;

import lombok.Data;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
public class Patient {
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    private LocalDate dateDeNaissance;
    private String genre;
    private String adresse;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "\\d{10,15}", message = "Le téléphone doit contenir 10 à 15 chiffres.")
    private String telephone;
}
