-Présentation du projet 

MedilaboSolutions est une application médicale dédiée à la gestion des patients, des notes médicales et à l’évaluation du risque de diabète.
L’application est conçue pour les secrétaires et les médecins, qui peuvent ajouter/modifier des patients, renseigner des notes médicales, et consulter une estimation du risque de diabète selon des critères médicaux précis.

-Prérequis

Avant de commencer, assurez-vous d’avoir installé sur votre machine :

Java 17

MySQL 8.x

MongoDB 7.x

Docker et Docker Compose (facultatif, recommandé pour un lancement rapide)

Un IDE Java (IntelliJ, Eclipse, VSCode…)

Java 17
MySQL 8.1
MongoDb 7.0.11
Docker (facultatif)
IDE comme Intellij / Eclipse

-Conception

Le projet repose sur une architecture microservices.
Il est composé de:

front-service : application front-end en Thymeleaf, pour l’interface utilisateur

gateway-service : passerelle d’API (Spring Cloud Gateway)

patient-service : gestion des patients, stockage MySQL

note-service : gestion des notes médicales, stockage MongoDB

diabetes-risk-service : calcul du risque de diabète

Bases de données : MySQL pour les patients, MongoDB pour les notes

Chaque microservice communique avec les autres via des APIs REST et la gateway, selon la logique suivante de:

![](C:\Users\hacha\Documents\structure.PNG)


-Sécurité

L’application utilise Spring Security.

Les utilisateurs doivent s’authentifier pour accéder aux fonctionnalités (connexion obligatoire).

Un JWT (token) est généré à la connexion et transmis dans l’en-tête de chaque requête.

La gateway et chaque microservice vérifient la validité du token avant d’autoriser l’accès aux ressources.

Les mots de passe sont stockés de manière sécurisée (BCrypt).

-Utilisation de MedilaboSolutions

Lancement avec Docker

1-Cloner le dépôt du projet de:

git clone https://github.com/ton-compte/medilabo-solutions.git
cd medilabo-solutions

2-Construire et lancer tous les services :

docker-compose up --build

3-Accéder à l’application via de:
http://localhost:8080


Lancement manuel (sans Docker)

1-Démarrer MySQL et MongoDB sur votre machine.

2-Configurer les accès BDD dans application.properties de chaque microservice (patient-service et note-service).

3-Compiler chaque microservice (via Maven).

4-Démarrer les microservices un par un (ordre conseillé : bases, patient, note, diabetes-risk, gateway, front).

5-Accéder à l’application via http://localhost:8080.

-Aperçu

Pages principales de l’application :

Page de connexion

Accueil (liste des patients)

Ajout/Modification d’un patient

Fiche patient (infos + notes médicales + risque de diabète)

Ajout/Modification d’une note médicale

-Green Code – Éco-conception logicielle

*Le niveau de log est limité à “info” pour réduire l’écriture sur disque et la consommation des ressources.

*Les données sensibles (adresse, téléphone, genre) ne sont affichées que sur la fiche patient, pas sur la liste globale.

*Le niveau de risque de diabète est stocké après calcul, pour éviter de recalculer à chaque consultation. On recalcule uniquement quand une note est ajoutée.

*Utilisation d’images Docker allégées (openjdk:17-jdk-alpine) pour limiter l’espace disque et l’énergie consommée lors des déploiements.

*Utilisation de Spring Data JPA pour éviter les requêtes “SELECT *” et ne charger que les données nécessaires.

-Suggestions d’amélioration

Mettre en place un cache sur les données peu volatiles.

Optimiser encore les requêtes SQL côté patient-service.

Ajouter une politique d’archivage/rotation des logs.

Mettre en place un monitoring (ex: Prometheus, Grafana) pour suivre la consommation CPU/mémoire des microservices.

Externaliser la configuration via Spring Cloud Config pour une gestion centralisée.

Ajouter la suppression d’un patient et la modification/suppression d’une note.

Ajouter un service de découverte (Eureka) pour rendre l’architecture encore plus dynamique.

Contact
Projet réalisé par ACHAHBOUNE hamid dans le cadre du parcours Développeur d’Application Java / OpenClassrooms.

