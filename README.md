# Gestion des Patients - Application Spring MVC avec Thymeleaf et Spring Data JPA

Ce projet est une application web développée en utilisant Spring MVC, Thymeleaf, et Spring Data JPA pour gérer une liste de patients. L'application permet d'afficher les patients, de faire de la pagination, de rechercher des patients par nom, et de supprimer des patients. Le projet est structuré pour être facilement extensible et modifiable.

## Fonctionnalités

- **Afficher les patients** : La liste des patients est affichée dans un tableau avec des informations telles que l'ID, le nom, la date de naissance, l'état de santé (malade ou non), et le score.
- **Pagination** : Les patients sont affichés par page, avec la possibilité de naviguer entre les pages.
- **Rechercher des patients** : Un champ de recherche permet de filtrer les patients par nom.
- **Supprimer un patient** : Chaque patient peut être supprimé avec une confirmation avant suppression.
- **Améliorations supplémentaires** : Le projet est conçu pour être facilement amélioré avec des fonctionnalités supplémentaires comme l'ajout ou la modification de patients.

---

## Structure du Projet

### 1. **Patient.java**
Cette classe représente l'entité `Patient` dans la base de données. Elle utilise des annotations JPA pour mapper les champs de la table.

```java
package com.example.activity3.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Builder
public class Patient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private Date dateNaissance;
    private boolean malade;
    private int score;
}
```

---

### 2. **PatientRepository.java**
Cette interface étend `JpaRepository` et fournit une méthode pour rechercher des patients par nom avec pagination.

```java
package com.example.activity3.Repositories;

import com.example.activity3.Entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Page<Patient> findByNomContains(String keyword, Pageable pageable);
}
```

---

### 3. **PatientController.java**
Ce contrôleur gère les requêtes HTTP pour afficher, rechercher et supprimer des patients.

```java
package com.example.activity3.Web;

import com.example.activity3.Entities.Patient;
import com.example.activity3.Repositories.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class PatientController {
    private PatientRepository patientRepository;

    @GetMapping("/patients")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "4") int size,
                        @RequestParam(name = "keyword", defaultValue = "") String kw) {
        Page<Patient> pagePatients = patientRepository.findByNomContains(kw, PageRequest.of(page, size));
        model.addAttribute("listPatients", pagePatients.getContent());
        model.addAttribute("pages", new int[pagePatients.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", kw);
        return "Patients";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam(name = "id") Long id,
                         @RequestParam(name = "page", defaultValue = "0") int page,
                         @RequestParam(name = "keyword", defaultValue = "") String kw) {
        patientRepository.deleteById(id);
        return "redirect:/patients?page=" + page + "&keyword=" + kw;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/patients";
    }
}
```

---

### 4. **Patients.html**
Ce fichier Thymeleaf affiche la liste des patients avec pagination et un formulaire de recherche.

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Liste des Patients</title>
    <link rel="stylesheet" href="/webjars/bootstrap/5.3.3/css/bootstrap.min.css">
    <link rel="stylesheet" href="/webjars/bootstrap-icons/1.11.3/font/bootstrap-icons.css">
</head>
<body>
<div class="p-3">
    <div class="card">
        <div class="card-header">Liste des patients</div>
        <div class="card-body">
            <form th:action="@{patients}" method="get">
                <label for="keyword">Keyword : </label>
                <input type="text" id="keyword" name="keyword" th:value="${keyword}">
                <button type="submit" class="btn btn-info"><i class="bi bi-search"></i></button>
            </form>
            <table class="table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Date</th>
                    <th>Malade</th>
                    <th>Score</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <tr th:each="patient : ${listPatients}">
                    <td th:text="${patient.id}"></td>
                    <td th:text="${patient.nom}"></td>
                    <td th:text="${patient.dateNaissance}"></td>
                    <td th:text="${patient.malade}"></td>
                    <td th:text="${patient.score}"></td>
                    <td>
                        <a th:href="@{delete(id=${patient.id},keyword=${keyword},page=${currentPage})}" 
                           class="btn btn-danger" 
                           onclick="return confirm('Êtes-vous sûr ?')">
                            <i class="bi bi-trash"></i>
                        </a>
                    </td>
                </tr>
                </tbody>
            </table>
            <ul class="pagination">
                <li class="page-item" th:each="page,status:${pages}">
                    <a class="page-link" 
                       th:href="@{/patients(page=${status.index},keyword=${keyword})}"
                       th:class="${currentPage==status.index ? 'btn btn-info ms-1':'btn btn-outline-info ms-1'}"
                       th:text="${status.index+1}">
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>
```

---

### 5. **application.properties**
Ce fichier configure la base de données MySQL et d'autres paramètres Spring.

```properties
spring.application.name=Activity3
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/hopital?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
spring.h2.console.enabled=true
```

---

### 6. **Activity3Application.java**
Ce fichier est le point d'entrée de l'application Spring Boot. Il initialise également quelques patients de test.

```java
package com.example.activity3;

import com.example.activity3.Entities.Patient;
import com.example.activity3.Repositories.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class Activity3Application {

    public static void main(String[] args) {
        SpringApplication.run(Activity3Application.class, args);
    }

    @Bean
    CommandLineRunner start(PatientRepository patientRepository) {
        return args -> {
            patientRepository.save(new Patient(null, "Mohamed", new Date(), false, 34));
            patientRepository.save(new Patient(null, "Hanane", new Date(), false, 4321));
            patientRepository.save(new Patient(null, "Imane", new Date(), true, 34));
        };
    }
}
```

---

## Installation et Exécution

1. Clonez le repository :
   ```bash
   git clone https://github.com/votre-utilisateur/votre-repository.git
   ```

2. Configurez la base de données dans `application.properties` avec vos informations de connexion MySQL.

3. Exécutez l'application :
   ```bash
   mvn spring-boot:run
   ```

4. Accédez à l'application via votre navigateur à l'adresse `http://localhost:8080`.

---

## Améliorations Possibles

- **Ajout de patients** : Implémenter une fonctionnalité pour ajouter de nouveaux patients.
- **Modification de patients** : Permettre la modification des informations d'un patient existant.
- **Validation des données** : Ajouter des validations pour les champs de saisie.
- **Sécurité** : Intégrer Spring Security pour gérer l'authentification et l'autorisation.
- **Tests** : Ajouter des tests unitaires et d'intégration pour garantir la qualité du code.

---

## Auteur

Ce projet a été développé par Zakaria El guazzar dans le cadre d'une activité pratique sur Spring MVC et Spring Data JPA.

---

## Licence

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

---

Ce `README.md` intègre désormais des extraits de code pertinents pour montrer comment chaque partie du projet fonctionne. Cela rend le projet plus facile à comprendre et à utiliser pour d'autres développeurs.