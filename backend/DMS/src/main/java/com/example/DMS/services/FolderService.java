package com.example.DMS.services;

import com.example.DMS.DTO.FolderDTO;
import com.example.DMS.config.JwtUtils;
import com.example.DMS.mappers.FolderMapper;
import com.example.DMS.models.Folder;
import com.example.DMS.repository.DocumentRepository;
import com.example.DMS.repository.FolderRepository;
import com.example.DMS.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;
    private final WorkspaceRepository workspaceRepository;
    private final JwtUtils currentUserService;
    private final FolderMapper folderMapper;
    private final DocumentRepository documentRepository;  // Add this


    public FolderDTO create(FolderDTO dto) {
        if (!workspaceRepository.existsById(dto.getWorkspaceId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Workspace not found");
        }

        if (folderRepository.existsByNameAndWorkspaceIdAndParentFolderId(
                dto.getName(),
                dto.getWorkspaceId(),
                dto.getParentFolderId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Folder with this name already exists in this location");
        }

        String userNid = currentUserService.getCurrentUserNid();

        Folder folder = new Folder();
        folder.setName(dto.getName());
        folder.setWorkspaceId(dto.getWorkspaceId());
        folder.setParentFolderId(dto.getParentFolderId());
        folder.setCreatedBy(userNid);
        folder.setCreatedAt(LocalDateTime.now());
        folder.setUpdatedAt(LocalDateTime.now());

        workspaceRepository.findById(dto.getWorkspaceId()).ifPresent(workspace -> {
            workspace.setFolderCount(workspace.getFolderCount() + 1);
            workspaceRepository.save(workspace);
        });

        return folderMapper.toDto(folderRepository.save(folder));
    }

    public List<FolderDTO> getByWorkspace(String workspaceId) {
        return folderRepository.findByWorkspaceId(workspaceId).stream()
                .filter(folder -> folder.getParentFolderId() == null)  // only root folders
                .map(folderMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<FolderDTO> getByParentFolder(String parentFolderId) {
        return folderRepository.findByParentFolderId(parentFolderId).stream()
                .map(folderMapper::toDto)
                .collect(Collectors.toList());
    }

    public FolderDTO getById(String folderId) {
        return folderRepository.findById(folderId)
                .map(folderMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Folder not found"));
    }

    public FolderDTO update(String folderId, FolderDTO dto) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Folder not found"));

        folder.setName(dto.getName());
        folder.setUpdatedAt(LocalDateTime.now());

        return folderMapper.toDto(folderRepository.save(folder));
    }

    public void delete(String folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Folder not found"));

        long subfolderCount = folderRepository.countByParentFolderId(folderId);
        if (subfolderCount > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot delete folder - it contains subfolders");
        }

        // Soft delete all documents in this folder
        documentRepository.findByFolderIdAndDeletedFalse(folderId).forEach(document -> {
            document.setDeleted(true);
            documentRepository.save(document);
        });

        // Update workspace folder count
        workspaceRepository.findById(folder.getWorkspaceId()).ifPresent(workspace -> {
            workspace.setFolderCount(workspace.getFolderCount() - 1);
            workspaceRepository.save(workspace);
        });

        // Delete the folder itself (hard delete)
        folderRepository.deleteById(folderId);
    }
}