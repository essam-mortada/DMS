package com.example.DMS.controllers;

import com.example.DMS.DTO.FolderDTO;
import com.example.DMS.models.Folder;
import com.example.DMS.services.FolderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {
    private final FolderService folderService;

    @PostMapping
    public ResponseEntity<FolderDTO> create(@RequestBody @Valid FolderDTO dto) {
        Folder folder = folderService.create(
                dto.getName()
                , dto.getWorkspaceId()
                , dto.getParentFolderId()

        );
        return ResponseEntity.ok(mapToDTO(folder));
    }

    @GetMapping("/{folderId}")
    public ResponseEntity<FolderDTO> getById(@PathVariable @NotBlank String folderId) {
        Folder folder = folderService.getById(folderId);
        return ResponseEntity.ok(mapToDTO(folder));
    }

    @GetMapping("/workspace/{workspaceId}")
    public ResponseEntity<List<FolderDTO>> getByWorkspace(@PathVariable @NotBlank String workspaceId) {
        List<Folder> folders = folderService.getByWorkspace(workspaceId);
        return ResponseEntity.ok(folders.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/parent/{parentFolderId}")
    public ResponseEntity<List<FolderDTO>> getByParentFolder(@PathVariable @NotBlank String parentFolderId) {
        List<Folder> folders = folderService.getByParentFolder(parentFolderId);
        return ResponseEntity.ok(folders.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList()));
    }

    @PutMapping("/{folderId}")
    public ResponseEntity<FolderDTO> update(@PathVariable @NotBlank String folderId, @RequestBody @Valid FolderDTO dto) {
        Folder folder = folderService.update(folderId, dto.getName());
        return ResponseEntity.ok(mapToDTO(folder));
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<Void> delete(@PathVariable @NotBlank String folderId) {
        folderService.delete(folderId);
        return ResponseEntity.noContent().build();
    }

    private FolderDTO mapToDTO(Folder folder) {
        return new FolderDTO(folder.getId(), folder.getName(), folder.getWorkspaceId(), folder.getParentFolderId());
    }

}