package com.medilabo.front.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {
    @GetMapping("/homePage")
    public String homePage() {
        return "homePage";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/patients")
    public String patientList() {
        return "patientInfoPage";
    }

    @GetMapping("/patients/add")
    public String addPatient() {
        return "addPatientPage";
    }
}
