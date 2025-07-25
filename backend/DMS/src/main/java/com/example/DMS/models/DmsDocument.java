package com.example.DMS.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

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
    private String folderId;
    private List<String> tags;
    private long  size;
    private boolean deleted = false;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}

