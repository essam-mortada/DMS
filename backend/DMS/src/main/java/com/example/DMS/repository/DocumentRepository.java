package com.example.DMS.repository;

import com.example.DMS.models.DmsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends MongoRepository<DmsDocument, String> {
    List<DmsDocument> findByWorkspaceIdAndDeletedFalse(String workspaceId);
    List<DmsDocument> findByOwnerNidAndDeletedFalse(String ownerNid);
    List<DmsDocument> findByNameContainingIgnoreCaseAndDeletedFalse(String keyword);
    List<DmsDocument> findByFolderIdAndDeletedFalse(String folderId);
    List<DmsDocument> findByOwnerNid(String ownerNid);
    List<DmsDocument> findByWorkspaceIdAndFolderIdAndDeletedFalse(String workspaceId, String folderId);
    List<DmsDocument> findByWorkspaceIdAndNameContainingIgnoreCaseAndDeletedFalse(String workspaceId, String keyword);
    List<DmsDocument> findByFolderIdAndNameContainingIgnoreCaseAndDeletedFalse(String folderId, String keyword);
    Optional<DmsDocument> findByShareLinkTokenAndIsLinkSharingEnabledTrue(String token);

}

