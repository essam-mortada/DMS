package com.example.DMS.mappers;

import com.example.DMS.DTO.FolderDTO;
import com.example.DMS.models.Folder;

public class FolderMapper {
    public FolderDTO toDto(Folder folder) {
        return new FolderDTO(
                folder.getId(),
                folder.getName(),
                folder.getWorkspaceId(),
                folder.getParentFolderId(),
                folder.getCreatedAt(),
                folder.getUpdatedAt()
        );
    }
}