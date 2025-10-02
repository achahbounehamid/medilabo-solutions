package com.medilabo.front.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;


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
