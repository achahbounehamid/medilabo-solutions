package com.note_service.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Entité MongoDB représentant une note médicale.
 *
 * <p>
 * Persistée dans la collection <b>notes</b>. Les horodatages
 * {@link #createdAt} et {@link #updatedAt} sont gérés automatiquement
 * par l’auditing Spring Data (annotations {@code @CreatedDate} et {@code @LastModifiedDate}).
 * </p>
 *
 * <p><b>Pré-requis :</b> activer l’auditing côté configuration avec {@code @EnableMongoAuditing}.</p>
 */
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

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;


}
