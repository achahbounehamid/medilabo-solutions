//package com.medilabo.diabetes_risk_service.service;
//
//
//import com.medilabo.diabetes_risk_service.client.NoteClient;
//import com.medilabo.diabetes_risk_service.client.PatientClient;
//import com.medilabo.diabetes_risk_service.model.NoteDto;
//import com.medilabo.diabetes_risk_service.model.PatientDto;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class DiabetesRiskServiceTest {
//
//    @Mock
//    private PatientClient patientClient;
//
//    @Mock
//    private NoteClient noteClient;
//
//    @InjectMocks
//    private DiabetesRiskService diabetesRiskService;
//
//    @Test
//    public void testCalculerRisque_AgeOver30_With4Triggers_ShouldReturnBorderline() {
//        // GIVEN
//        Long patientId = 1L;
//
//        PatientDto patient = PatientDto.builder()
//                .id(1L)
//                .dateDeNaissance(LocalDate.of(1980, 1, 1))
//                .genre("M")
//                .build();
//
//
//        List<NoteDto> notes = List.of(
//                new NoteDto(1, "Patient has high hemoglobin and cholesterol."),
//                new NoteDto(1, "Reports abnormal dizziness.")
//        );
//
//        when(patientClient.getPatientById(patientId)).thenReturn(patient);
//        when(noteClient.getNotesByPatientId(patientId.intValue())).thenReturn(notes);
//
//        // WHEN
//        String result = diabetesRiskService.calculerRisque(patientId);
//
//        // THEN
//        assertEquals("Borderline", result);
//    }
//}
