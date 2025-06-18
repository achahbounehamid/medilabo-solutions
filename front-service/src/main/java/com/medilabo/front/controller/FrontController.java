package com.medilabo.front.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {
    @GetMapping("/")
    public String home() {
        return "home"; // Affiche home.html
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // Affiche login.html
    }

    @GetMapping("/patients")
    public String patientList() {
        return "patient"; // Affiche patient.html
    }

    @GetMapping("/patients/add")
    public String addPatient() {
        return "addPatient"; // Affiche addPatient.html
    }
}
