package com.example.DMS.mappers;

import com.example.DMS.DTO.WorkspaceDTO;
import com.example.DMS.DTO.userDTO;
import com.example.DMS.models.User;
import com.example.DMS.models.Workspace;

public class workspaceMapper {
    public  WorkspaceDTO toDTO(Workspace workspace) {
        WorkspaceDTO dto = new WorkspaceDTO();
        dto.setName(workspace.getName());
        dto.setUserNid(workspace.getUserNid());
        dto.setId(workspace.getId());
        dto.setCreatedAt(workspace.getCreatedAt());
        return dto;
    }
}
