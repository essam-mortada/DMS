package com.example.DMS.mappers;

import com.example.DMS.DTO.DocumentDTO;
import com.example.DMS.models.DmsDocument;

public class DocumentMapper {
    public DocumentDTO toDto(DmsDocument doc) {
        DocumentDTO dto = new DocumentDTO();
        dto.setId(doc.getId());
        dto.setName(doc.getName());
        dto.setType(doc.getType());
        dto.setOwnerNid(doc.getOwnerNid());
        dto.setWorkspaceId(doc.getWorkspaceId());
        dto.setFolderId(doc.getFolderId());
        dto.setSize(doc.getSize());
        dto.setTags(doc.getTags());
        dto.setDeleted(doc.isDeleted());
        dto.setCreatedAt(doc.getCreatedAt());

        return dto;
    }
}
