package com.medilabo.diabetes_risk_service.client;


import com.medilabo.diabetes_risk_service.model.NoteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "note-service", url = "${note.service.url}")
public interface NoteClient {
    @GetMapping("/api/notes/patient/{patientId}")
    List<NoteDto> getNotesByPatientId(@PathVariable("patientId") Integer patientId);
}

