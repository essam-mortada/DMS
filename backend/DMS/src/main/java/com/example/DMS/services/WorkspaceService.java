
package com.example.DMS.services;

import com.example.DMS.config.JwtUtils;
import com.example.DMS.models.Workspace;
import com.example.DMS.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final JwtUtils currentUserService;

    public Workspace create(String name) {
        String userNid = currentUserService.getCurrentUserNid();

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Workspace name cannot be empty");
        }

        Workspace ws = Workspace.builder()
                .name(name)
                .userNid(userNid)
                .build();
        return workspaceRepository.save(ws);
    }

    public List<Workspace> getCurrentUserWorkspaces() {
        String userNid = currentUserService.getCurrentUserNid();
        return workspaceRepository.findByUserNid(userNid);
    }

    public Workspace getWorkspaceById(String id) {
        return workspaceRepository.findById(id).orElse(null);
    }

    public Workspace update(String id, String name) {
        Workspace ws = getWorkspaceById(id);
        ws.setName(name);
        return workspaceRepository.save(ws);
    }

    public void delete(String id) {
        if (workspaceRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Workspace does not exist");
        }
        workspaceRepository.deleteById(id);
    }
}