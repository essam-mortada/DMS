package com.example.DMS.DTO;

import com.example.DMS.models.Folder;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderDTO {
    private String id;
    @NotBlank(message = "Folder name is required")
    private String name;
    private String workspaceId;

    private String parentFolderId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public FolderDTO(Folder folder) {
        this.name = folder.getName();
        this.id = folder.getId();
        this.workspaceId = folder.getWorkspaceId();
        this.parentFolderId = folder.getParentFolderId();


    }
}