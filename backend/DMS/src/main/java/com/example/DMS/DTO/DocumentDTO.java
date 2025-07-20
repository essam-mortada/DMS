package com.example.DMS.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DocumentDTO {
    private String id;
    private String name;
    private String type;
    private String ownerNid;
    private String workspaceId;
    private String folderId;
    private List<String> tags;
    private boolean deleted;
    private LocalDateTime createdAt;
}
