package com.example.DMS.controllers;

import com.example.DMS.DTO.FolderDTO;
import com.example.DMS.services.FolderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {
    private final FolderService folderService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FolderDTO> create(@RequestBody @Valid FolderDTO dto) {
        return ResponseEntity.ok(folderService.create(dto));
    }

    @GetMapping("/{folderId}")
    @PreAuthorize("@sharingService.hasPermissionToFolder(#folderId, 'VIEW')")
    public ResponseEntity<FolderDTO> getById(@PathVariable @NotBlank String folderId) {
        return ResponseEntity.ok(folderService.getById(folderId));
    }

    @GetMapping("/workspace/{workspaceId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<FolderDTO>> getByWorkspace(@PathVariable @NotBlank String workspaceId) {
        return ResponseEntity.ok(folderService.getByWorkspace(workspaceId));
    }

    @GetMapping("/parent/{parentFolderId}")
    @PreAuthorize("@sharingService.hasPermissionToFolder(#parentFolderId, 'VIEW')")
    public ResponseEntity<List<FolderDTO>> getByParentFolder(@PathVariable @NotBlank String parentFolderId) {
        return ResponseEntity.ok(folderService.getByParentFolder(parentFolderId));
    }

    @PutMapping("/{folderId}")
    @PreAuthorize("@sharingService.hasPermissionToFolder(#folderId, 'EDIT')")
    public ResponseEntity<FolderDTO> update(
            @PathVariable @NotBlank String folderId,
            @RequestBody @Valid FolderDTO dto) {
        return ResponseEntity.ok(folderService.update(folderId, dto));
    }

    @DeleteMapping("/{folderId}")
    @PreAuthorize("@sharingService.hasPermissionToFolder(#folderId, 'EDIT')")
    public ResponseEntity<Void> delete(@PathVariable @NotBlank String folderId) {
        folderService.delete(folderId);
        return ResponseEntity.noContent().build();
    }
}