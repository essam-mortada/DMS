package com.example.DMS.models;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "workspaces")
public class Workspace {

    @Id
    private String id;

    private String name;
    private String ownerNid;
    private LocalDateTime createdAt;
}
