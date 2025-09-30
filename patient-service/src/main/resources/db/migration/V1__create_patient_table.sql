-- V1 : créer la table patient
CREATE TABLE IF NOT EXISTS patient (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  prenom VARCHAR(100) NOT NULL,
  nom VARCHAR(100) NOT NULL,
  date_de_naissance DATE NOT NULL,
  genre CHAR(1) NOT NULL,
  adresse VARCHAR(255),
  telephone VARCHAR(50)
);
