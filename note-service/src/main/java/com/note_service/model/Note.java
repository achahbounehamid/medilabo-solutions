package com.note_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Note {
    @Id
    private String id;

    private Integer patientId;
    private String content;
   private LocalDateTime createdAt;
}
