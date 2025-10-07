//package com.medilabo.diabetes_risk_service.controller;
//
//import com.medilabo.diabetes_risk_service.service.DiabetesRiskService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(DiabetesRiskController.class)
//public class DiabetesRiskControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private DiabetesRiskService diabetesRiskService; // Ajouté
//
//    @Test
//    public void testGetRisk_ReturnsCorrectRiskLevel() throws Exception {
//        // GIVEN
//        Long patientId = 1L;
//
//        when(diabetesRiskService.calculerRisque(patientId)).thenReturn("Borderline");
//
//        // WHEN + THEN
//        mockMvc.perform(get("/api/diabetes-risk/{patientId}", patientId))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Borderline"));
//    }
//}
//
//
