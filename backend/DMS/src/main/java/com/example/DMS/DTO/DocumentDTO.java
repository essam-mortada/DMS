package com.example.DMS.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DocumentDTO {

    @NotBlank(message = "Document name is required")
    private String name;

    @NotBlank(message = "Document type is required")
    private String type;

    @NotBlank(message = "Owner NID is required")
    private String ownerNid;

    @NotBlank(message = "Workspace ID is required")
    private String workspaceId;
}
