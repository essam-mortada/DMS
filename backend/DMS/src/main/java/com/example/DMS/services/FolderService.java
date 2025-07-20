package com.example.DMS.services;

import com.example.DMS.config.JwtUtils;
import com.example.DMS.models.Folder;
import com.example.DMS.repository.FolderRepository;
import com.example.DMS.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;
    private final WorkspaceRepository workspaceRepository;
    private final JwtUtils currentUserService;

    public Folder create(String name, String workspaceId, String parentFolderId) {
        // Validate workspace exists
        System.out.println("Workspace ID: " + workspaceId);

        if (!workspaceRepository.existsById(workspaceId)) {
            throw new IllegalArgumentException("Workspace not found");
        }

        // Check if folder with same name already exists in the same location
        if (folderRepository.existsByNameAndWorkspaceIdAndParentFolderId(name, workspaceId, parentFolderId)) {
            throw new IllegalArgumentException("Folder with this name already exists in this location");
        }

        String userNid = currentUserService.getCurrentUserNid();

        Folder folder = Folder.builder()
                .name(name)
                .workspaceId(workspaceId)
                .parentFolderId(parentFolderId)
                .createdBy(userNid)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

         //Update workspace folder count
        workspaceRepository.findById(workspaceId).ifPresent(workspace -> {
            workspace.setFolderCount(workspace.getFolderCount() + 1);
            workspaceRepository.save(workspace);
        });

        return folderRepository.save(folder);
    }

    public List<Folder> getByWorkspace(String workspaceId) {
        return folderRepository.findByWorkspaceId(workspaceId);
    }

    public List<Folder> getByParentFolder(String parentFolderId) {
        return folderRepository.findByParentFolderId(parentFolderId);
    }

    public Folder getById(String folderId) {
        return folderRepository.findById(folderId)
                .orElseThrow(() -> new EmptyResultDataAccessException("Folder not found", 1));
    }

    public Folder update(String folderId, String name) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new EmptyResultDataAccessException("Folder not found", 1));
        folder.setName(name);
        return folderRepository.save(folder);
    }

    public void delete(String folderId) {
        // Check if folder exists
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new EmptyResultDataAccessException("Folder not found", 1));

        // Check if folder is empty (no subfolders or documents)
        long subfolderCount = folderRepository.countByParentFolderId(folderId);
        // You would also check for documents here in a real implementation

        if (subfolderCount > 0) {
            throw new IllegalStateException("Cannot delete folder - it contains subfolders");
        }

        // Update workspace folder count
        workspaceRepository.findById(folder.getWorkspaceId()).ifPresent(workspace -> {
            workspace.setFolderCount(workspace.getFolderCount() - 1);
            workspaceRepository.save(workspace);
        });

        folderRepository.deleteById(folderId);
    }
}