package com.example.DMS.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadDocumentRequest {
    private String name;
    private String type;
    @PositiveOrZero
    private Long size;
    @NotNull
    private String workspaceId;
    private String folderId;

    public long getSizeOrDefault(long defaultValue) {
        return size != null ? size : defaultValue;
    }
}
