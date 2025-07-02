package com.medilabo.front.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Note {
    private String id;
    private Integer patientId;
    private String content;
    private LocalDateTime createdAt;
}
