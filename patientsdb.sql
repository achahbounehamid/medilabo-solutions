-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : ven. 15 août 2025 à 22:31
-- Version du serveur : 8.3.0
-- Version de PHP : 8.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `patientsdb`
--

-- --------------------------------------------------------

--
-- Structure de la table `patient`
--

DROP TABLE IF EXISTS `patient`;
CREATE TABLE IF NOT EXISTS `patient` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `adresse` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `date_de_naissance` date DEFAULT NULL,
  `genre` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nom` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `prenom` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `telephone` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `patient`
--

INSERT INTO `patient` (`id`, `adresse`, `date_de_naissance`, `genre`, `nom`, `prenom`, `telephone`) VALUES
(1, '14 rue du Soleil', '2021-03-03', 'F', 'Martin', 'coco', '0601020304'),
(2, '25 avenue des Lilas', '1985-05-12', 'F', 'Durand', 'Sophie', '0600000000'),
(4, '45 bv victor hugo', '2005-02-02', 'M', 'didi', 'coco', '00000000000000000000'),
(5, '11 rue walo paris', '1950-01-01', 'M', 'ali', 'alexander', '0601020304'),
(6, '45 bv hugo ', '1987-09-01', 'M', 'achahboune', 'hamid', '010203040506');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
