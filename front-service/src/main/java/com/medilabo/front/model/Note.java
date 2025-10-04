package com.medilabo.front.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * Modèle représentant une note médicale côté front-service.
 *
 * <p>
 * Cette classe correspond au format JSON renvoyé par le <b>note-service</b>
 * et est utilisée pour afficher et manipuler les notes dans les pages Thymeleaf.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Note {
    private String id;
    private Integer patientId;
    private String content;
//    private OffsetDateTime createdAt;
@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]XXX")
private OffsetDateTime createdAt;


}
