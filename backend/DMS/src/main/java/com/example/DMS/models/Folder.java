package com.example.DMS.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "folders")
public class Folder {
    @Id
    private String id;
    private String name;
    private String workspaceId;
    private String parentFolderId;
    private String createdBy; // User NID
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String shareLinkToken;
    private SharePermission shareLinkPermission;
    private boolean isLinkSharingEnabled = false;

    public String getOwnerNid() {
        return createdBy;
    }
}