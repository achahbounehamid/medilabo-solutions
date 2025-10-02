# MedilaboSolutions

MedilaboSolutions est une application médicale permettant de :
- gérer les **patients** (MySQL),
- enregistrer des **notes médicales** (MongoDB),
- **évaluer le risque de diabète** (microservice dédié).

Public cible : **secrétaires** & **médecins**.  
UI réalisée avec **Spring Boot + Thymeleaf**.

---

## Sommaire
- [Architecture](#architecture)
- [Services & Ports](#services--ports)
- [Aperçu (captures)](#aperçu-captures)
- [Prérequis](#prérequis)
- [Démarrage rapide (Docker)](#démarrage-rapide-docker)
- [Configuration](#configuration)
- [Préchargement des données (où et comment)](#préchargement-des-données-où-et-comment)
- [Lancement manuel (sans Docker)](#lancement-manuel-sans-docker)
- [Sécurité](#sécurité)
- [API (référence rapide)](#api-référence-rapide)
- [Green Code](#green-code)
- [Améliorations possibles](#améliorations-possibles)
- [Crédits](#crédits)

---

## Architecture

L’application est composée de 4 microservices + 1 passerelle :

```
Utilisateur → Front-end (Thymeleaf)
               │
               ▼
           Gateway (API)
        ╱      │        ╲
Patient Svc   Note Svc   Diabetes-Risk Svc
 (MySQL)      (MongoDB)      (règles métier)
```

> Schéma :  
> ![Architecture](C:\wamp64\www\openclassrooms\P9-Developpez-une-solution-en-microservices-pour-votre-client\conception.PNG)

---

## Services & Ports

| Service                   | Port | Rôle                          | URL locale                                      |
|---------------------------|------|-------------------------------|-------------------------------------------------|
| **front-service**         | 8081 | UI (Thymeleaf)                | http://localhost:8081                           |
| **gateway-service**       | 8080 | Reverse proxy / API           | http://localhost:8080                           |
| **patient-service**       | 9001 | API Patients (MySQL)          | http://localhost:9001/api/patients              |
| **note-service**          | 9002 | API Notes (MongoDB)           | http://localhost:9002/api/notes                 |
| **diabetes-risk-service** | 9003 | API Risque de diabète         | http://localhost:9003/api/diabetes-risk/{id}    |
| **MongoDB**               | 27017| Base NoSQL                    | mongodb://localhost:27017                       |

---

## Aperçu (captures)

- **Login**  
  ![Login](C:\wamp64\www\openclassrooms\P9-Developpez-une-solution-en-microservices-pour-votre-client\login.PNG)

- **Page d’accueil**  
  ![Home](C:\wamp64\www\openclassrooms\P9-Developpez-une-solution-en-microservices-pour-votre-client\homePage.PNG)
- **Fiche patient (ajout un patient)**
- ![Ajouter un  patient](C:\wamp64\www\openclassrooms\P9-Developpez-une-solution-en-microservices-pour-votre-client\AjoutePatient.PNG)
- **Fiche patient (notes + risque)**  
  ![Détails patient](C:\wamp64\www\openclassrooms\P9-Developpez-une-solution-en-microservices-pour-votre-client\pageDetailsPatient.PNG)

---

## Prérequis

- **Java 17**
- **MySQL 8.x**
- **MongoDB** (6.x/7.x)
- **Docker & Docker Compose** (recommandé)
- IDE Java (IntelliJ IDEA)

---

## Démarrage rapide (Docker)

```bash
git clone <repo>
cd medilabo-solutions
docker compose up --build
```

- UI : http://localhost:8081
- API via gateway : http://localhost:8080

---

## Configuration

### Variables d’environnement (front-service)

Définies dans `docker-compose.yml` :
```yaml
environment:
  SERVER_PORT: 8081
  PATIENT_SERVICE_URL: http://patient-service:9001
  NOTE_SERVICE_URL: http://note-service:9002
  RISK_SERVICE_URL: http://diabetes-risk-service:9003
```

Défauts (fichier) dans `front-service/src/main/resources/application.properties` :
```properties
server.port=8081
patient.service.url=http://patient-service:9001
note.service.url=http://note-service:9002
risk.service.url=http://localhost:9003
spring.thymeleaf.cache=false
```


### Datasources
- **patient-service** : MySQL 
- **note-service** : `spring.data.mongodb.uri=mongodb://mongo:27017/notedb` (Docker)  
  ou `mongodb://localhost:27017/notedb` (local)

---

## Préchargement des données (où et comment)

### Patients (MySQL) — **Flyway**
Les patients de démo sont insérés automatiquement au démarrage via **Flyway** :

- Dossiers :  
  `patient-service/src/main/resources/db/migration/`
- Exemples de fichiers :
    - `V1__create_tables.sql` (schéma)
    - `V2__seed_patients.sql` (données de démo)

> Les logs montrent l’exécution Flyway au boot (`Successfully validated ...`, `Schema up to date`).

### Notes (MongoDB) — **Initialiseur Spring**
Les notes de démo peuvent être chargées au démarrage du **note-service** via un initialiseur :

- Classe (ex.) :  
  `note-service/src/main/java/.../config/DataInitializer.java`  

- Ressource (ex.) :  
  `note-service/src/main/resources/data/notes-demo.json`


### Risque de diabète
Pas de données à précharger : le service calcule le **niveau de risque**
(None / Borderline / In Danger / Early onset) à la demande, à partir des notes
et du profil patient.

---

## Lancement manuel (sans Docker)

1. **Démarrer MySQL** et créer la base `patientsdb`.
2. **Démarrer MongoDB** et créer la base `notedb` (ou laissez Spring le faire).
3. Configurer :
    - `patient-service/src/main/resources/application.properties` (JDBC MySQL)
    - `note-service/src/main/resources/application.properties` (URI MongoDB)
4. Construire :
   ```bash
   mvn -q -DskipTests package
   ```
5. Démarrer les services **dans cet ordre** :
    1) `patient-service` → 2) `note-service` → 3) `diabetes-risk-service` → 4) `gateway-service` → 5) `front-service`
6. Ouvrir l’UI : http://localhost:8081


---

## Sécurité

- **Form Login** (Spring Security) côté **front-service**.
- Ressources statiques autorisées : `/webjars/**`, `/css/**`, `/js/**`, `/images/**`, `/assets/**`.
- Après login, redirection vers `/homePage`.

---

## API (référence rapide)

### patient-service
- `GET /api/patients` — lister
- `GET /api/patients/{id}` — détail
- `POST /api/patients` — créer
- `PUT /api/patients/{id}` — modifier
- `DELETE /api/patients/{id}` — supprimer

### note-service
- `GET /api/notes/patient/{patientId}` — notes d’un patient
- `GET /api/notes/{id}` — détail d’une note
- `POST /api/notes` — créer (body: `{ patientId, content }`)
- `PUT /api/notes/{id}` — modifier
- `DELETE /api/notes/{id}` — supprimer

### diabetes-risk-service
- `GET /api/diabetes-risk/{patientId}` — niveau de risque calculé

---

## Green Code

Bonnes pratiques mises en place (ou conseillées) pour réduire l’empreinte carbone et améliorer l’efficacité :

1. **Logs parcimonieux**
    - Niveau par défaut : `INFO` (pas de `DEBUG` en prod).
    - Rotation des logs pour limiter l’I/O disque.
   ```properties
   # application.properties (ex. front-service)
   logging.level.root=info
   ```

2. **Images Docker allégées**
    - Build **multi-étapes** + base.
    - Copier uniquement le JAR final.
   ```dockerfile
   # Étape 1: build
   FROM maven
   WORKDIR /app
   COPY . .
   RUN mvn -q -DskipTests package

   # Étape 2: runtime
   FROM maven 
   WORKDIR /
   COPY --from=build /app/target/*.jar /app.jar
   ENTRYPOINT ["java","-jar","/app.jar"]
   ```

3. **Compression HTTP** (moins de bande passante)
   ```properties
   # application.properties (gateway/front)
   server.compression.enabled=true
   server.compression.mime-types=application/json,text/html,text/plain,text/css,application/javascript
   server.compression.min-response-size=1024
   ```

4. **Pagination côté API & UI**
    - Éviter de charger toute la base en une fois.
    - Ajouter `page`/`size` sur `/api/patients` et adapter l’UI.

5. **Caches ciblés**
    - Mise en cache des résultats de risque pour un patient pendant X minutes.
    - Cacher les ressources statiques (WebJars) via en-têtes HTTP.

6. **Requêtes efficaces**
    - Indexer les champs filtrés (MongoDB: `patientId`, dates).
    - Vérifier les jointures et éviter le N+1 côté JPA.

7. **Limiter les ressources des conteneurs** (Compose/K8s)
   ```yaml
   services:
     patient-service:
       deploy:
         resources:
           limits:
             cpus: '0.50'
             memory: 512M
   ```

8. **Désactiver ce qui est inutile en prod**
    - `spring.thymeleaf.cache=true` (prod).
    - DevTools uniquement en dev.
---

## Améliorations possibles

- Authentification **JWT / OAuth2**
- Mise en cache des calculs de risque
- Monitoring (Actuator, Prometheus/Grafana)
- Spring Cloud Config / Eureka
- Index MongoDB & optimisations SQL
- Politique de rotation des logs

---

## Crédits

Projet réalisé par **ACHAHBOUNE Hamid** – Parcours Développeur d’Application Java, OpenClassrooms.