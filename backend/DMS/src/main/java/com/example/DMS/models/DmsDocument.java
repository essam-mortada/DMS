package com.example.DMS.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "documents")
public class DmsDocument {

    @Id
    private String id;

    private String name;
    private String type;
    private String url;

    private String ownerNid;
    private String workspaceId;

    private boolean deleted = false;

    private LocalDateTime uploadedAt = LocalDateTime.now();
}
