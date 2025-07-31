package com.example.DMS.services;

import com.example.DMS.models.DmsDocument;
import com.example.DMS.models.Folder;
import com.example.DMS.repository.DocumentRepository;
import com.example.DMS.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SharingService {

    private final DocumentRepository documentRepository;
    private final FolderRepository folderRepository;

    public boolean hasPermission(String entityId, String ownerNid, String entityType) {
        if ("document".equals(entityType)) {
            return documentRepository.findById(entityId)
                    .map(d -> d.getOwnerNid().equals(ownerNid))
                    .orElse(false);
        } else if ("folder".equals(entityType)) {
            return folderRepository.findById(entityId)
                    .map(f -> f.getOwnerNid().equals(ownerNid))
                    .orElse(false);
        }
        return false;
    }
}
