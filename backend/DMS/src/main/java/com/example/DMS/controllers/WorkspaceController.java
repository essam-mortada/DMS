package com.example.DMS.controllers;

import com.example.DMS.DTO.WorkspaceDTO;
import com.example.DMS.models.Workspace;
import com.example.DMS.services.WorkspaceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.ErrorManager;
@Slf4j
@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<?> createWorkspace(@RequestBody @Valid WorkspaceDTO request) {
        try {
            Workspace ws = workspaceService.create(request.getName());
            return ResponseEntity.ok(new WorkspaceDTO(ws.getName(), ws.getUserNid(), ws.getId()));
        } catch (Exception e) {
            log.error("Workspace creation failed", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserWorkspaces() {
        try {
            List<Workspace> workspaces = workspaceService.getCurrentUserWorkspaces();
            if (workspaces.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(workspaces.stream()
                    .map(ws -> new WorkspaceDTO(ws.getName(), ws.getUserNid(), ws.getId()))
                    .toList());
        } catch (Exception e) {
            log.error("Failed to fetch workspaces", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{workspaceId}")
    public ResponseEntity<?> getWorkspaceById(@PathVariable @NotBlank String workspaceId) {
        try {
            Workspace ws = workspaceService.getWorkspaceById(workspaceId);
            return ResponseEntity.ok(new WorkspaceDTO(ws.getName(), ws.getUserNid() , ws.getId()));
        } catch (Exception e) {
            log.error("Failed to fetch workspace", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<?> deleteWorkspace(@PathVariable @NotBlank String workspaceId) {
        try {
            workspaceService.delete(workspaceId);
            return ResponseEntity.noContent().build();
        } catch (DataAccessException e) {
            log.error("Failed to delete workspace", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{workspaceId}")
    public ResponseEntity<?> updateWorkspace(@PathVariable @NotBlank String workspaceId, @RequestBody @Valid WorkspaceDTO request) {
        try {
            Workspace ws = workspaceService.getWorkspaceById(workspaceId);
            ws.setName(request.getName());
            workspaceService.update(workspaceId, request.getName());
            return ResponseEntity.ok(new WorkspaceDTO(ws.getName(), ws.getUserNid(), ws.getId()));
        } catch (Exception e) {
            log.error("Failed to update workspace", e);
            return ResponseEntity.internalServerError().build();
        }
    }

}


