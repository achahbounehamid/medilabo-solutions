Présentation

MedilaboSolutions est une application médicale pour :

gérer les patients (MySQL),

enregistrer des notes médicales (MongoDB),

évaluer le risque de diabète (microservice dédié).

Public cible : secrétaires & médecins.

Architecture

front-service (Thymeleaf, port 8081) – UI

gateway-service (Spring Cloud Gateway, port 8080) – reverse proxy/API

patient-service (Spring Boot, JPA/Hibernate, MySQL, port 9001)

note-service (Spring Boot, Spring Data MongoDB, port 9002)

diabetes-risk-service (Spring Boot, port 9003)

Ports & URLs
Service	Port	Rôle	           URL
Front	8081	UI  	           http://localhost:8081

Gateway	8080	Proxy API	       http://localhost:8080

Patient	9001	API Patients       http://localhost:9001/api/patients

Note	9002	API Notes	       http://localhost:9002/api/notes

Risk	9003	API Risque	       http://localhost:9003/api/risk

MongoDB	27017	DB NoSQL	       mongodb://localhost:27017

Les appels UI → API passent via la gateway (/api/**).

Prérequis

Java 17

MySQL 8.x

MongoDB 4.4 (ou monte l’image et change ce texte si tu vises 7.x)

Docker & Docker Compose

IDE Java (IntelliJ)

Lancement avec Docker
git clone <repo>
cd medilabo-solutions
docker compose up --build


UI : http://localhost:8081

API (via gateway) : http://localhost:8080

Si MySQL est local (WAMP/XAMPP), patient-service utilise :
SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/patientsdb?...
Identifiants à renseigner dans docker-compose.yml.

Lancement manuel

Démarrer MySQL & MongoDB

Configurer application.properties :

patient-service → datasource MySQL

note-service → spring.data.mongodb.uri=mongodb://localhost:27017/notedb

mvn clean package sur chaque service

Démarrer l’ordre : patient → note → risk → gateway → front

UI : http://localhost:8081

Sécurité

Authentification form-login (Spring Security).

Les ressources statiques & WebJars sont autorisées (/webjars/**, /css/**, …).

Fonctionnalités (par sprint)

Sprint 1 – Patients

Affichage des infos nom, prénom, date de naissance, genre, adresse, téléphone.

Ajout & Modification d’un patient.

Adresse & téléphone sont optionnels.

Sprint 2 – Notes (MongoDB)

Affichage de l’historique des notes d’un patient.

Ajout/Modification/Suppression d’une note.

Mise en forme conservée (retours à la ligne) côté UI :

<td style="white-space: pre-wrap" th :text="${note.content}"></td>

Sprint 3 – Rapport de risque de diabète

Calcul du risque : None / Borderline / In Danger / Early onset

Règles métier implémentées (âge + genre + nb de déclencheurs).

Déclencheurs recherchés dans les notes : Hemoglobine A1C, Microalbumin, Height, Weight, Smoker/Fumeur/Fumeuse, Abnormal/Anormal, Cholesterol/Cholestérol, Dizziness/Vertiges, Relapse/Rechute, Reaction/Réaction, Antibodies/Anticorps…
(liste complète dans le code du service de risque)

Jeu de données (démo)

Patients : quelques entrées de test en MySQL.

Notes : import des cas de tests fournis (Sprint 2) dans MongoDB (notedb.notes).
Exemple d’insertion :

{ "patientId": 81, "content": "Le patient déclare ...", "createdAt": "2025-09-23T19:33:30Z" }

Green Code

Logs en INFO (limite l’I/O disque).

Images Docker allégées.

Données privées non affichées sur les listes (affichage détaillé en fiche).

(Optionnel) Cache sur résultats de risque si besoin.

Améliorations possibles

JWT / OAuth2

Cache des calculs

Monitoring (Prometheus/Grafana)

Spring Cloud Config / Eureka

Optimisations SQL & index Mongo

Politique de rotation des logs

Contact

Projet réalisé par ACHAHBOUNE Hamid – Parcours Développeur d’Application Java, OpenClassrooms.
