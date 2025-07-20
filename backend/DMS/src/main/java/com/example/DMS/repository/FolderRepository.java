package com.example.DMS.repository;

import com.example.DMS.models.Folder;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface FolderRepository extends MongoRepository<Folder, String> {
    List<Folder> findByWorkspaceId(String workspaceId);
    List<Folder> findByParentFolderId(String parentFolderId);
    List<Folder> findByWorkspaceIdAndParentFolderId(String workspaceId, String parentFolderId);
    boolean existsByNameAndWorkspaceIdAndParentFolderId(String name, String workspaceId, String parentFolderId);

    long countByParentFolderId(String folderId);
}