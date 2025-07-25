package com.example.DMS.controllers;

import com.example.DMS.DTO.WorkspaceDTO;
import com.example.DMS.mappers.workspaceMapper;
import com.example.DMS.services.WorkspaceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<WorkspaceDTO> createWorkspace(@RequestBody @Valid WorkspaceDTO request) {
        try {
            return ResponseEntity.ok(workspaceService.create(request));
        } catch (Exception e) {
            log.error("Workspace creation failed", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<WorkspaceDTO>> getUserWorkspaces() {
        try {
            List<WorkspaceDTO> workspaces = workspaceService.getCurrentUserWorkspaces();
            if (workspaces.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(workspaces);
        } catch (Exception e) {
            log.error("Failed to fetch workspaces", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceDTO> getWorkspaceById(@PathVariable @NotBlank String workspaceId) {
        try {
            return ResponseEntity.ok(workspaceService.getWorkspaceById(workspaceId));
        } catch (Exception e) {
            log.error("Failed to fetch workspace", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable @NotBlank String workspaceId) {
        try {
            workspaceService.delete(workspaceId);
            return ResponseEntity.noContent().build();
        } catch (DataAccessException e) {
            log.error("Failed to delete workspace", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceDTO> updateWorkspace(
            @PathVariable @NotBlank String workspaceId,
            @RequestBody @Valid WorkspaceDTO request) {
        try {
            return ResponseEntity.ok(workspaceService.update(workspaceId, request));
        } catch (Exception e) {
            log.error("Failed to update workspace", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}