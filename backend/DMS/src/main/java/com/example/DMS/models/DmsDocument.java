package com.example.DMS.models;

import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "documents")
public class DmsDocument  {

    @Id
    private String id;

    private String name;
    private String type;
    private String workspaceId;
    private String ownerNid;

    private Map<String, Object> metadata;
    private String fileUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

