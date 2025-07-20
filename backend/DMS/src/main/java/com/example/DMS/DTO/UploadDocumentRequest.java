package com.example.DMS.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadDocumentRequest {
    private String name;
    private String type;
    private String workspaceId;
    private String folderId;
}
