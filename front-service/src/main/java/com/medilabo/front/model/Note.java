package com.medilabo.front.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Note {
    private String id;
    private Integer patientId;
    private String content;
    private LocalDateTime createdAt;
//
//    private OffsetDateTime createdAt; // ou LocalDateTime / Instant


}
