package com.example.DMS.services;

import com.example.DMS.DTO.WorkspaceDTO;
import com.example.DMS.config.JwtUtils;
import com.example.DMS.mappers.workspaceMapper;
import com.example.DMS.models.Workspace;
import com.example.DMS.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final JwtUtils currentUserService;
    private final workspaceMapper workspaceMapper;

    public WorkspaceDTO create(WorkspaceDTO request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Workspace name cannot be empty");
        }

        String userNid = currentUserService.getCurrentUserNid();

        Workspace ws = Workspace.builder()
                .name(request.getName())
                .userNid(userNid)
                .build();

        return workspaceMapper.toDTO(workspaceRepository.save(ws));
    }

    public List<WorkspaceDTO> getCurrentUserWorkspaces() {
        String userNid = currentUserService.getCurrentUserNid();
        return workspaceRepository.findByUserNid(userNid).stream()
                .map(workspaceMapper::toDTO)
                .collect(Collectors.toList());
    }

    public WorkspaceDTO getWorkspaceById(String id) {
        if(workspaceRepository.findByUserNid(currentUserService.getCurrentUserNid()).stream().noneMatch(ws -> ws.getId().equals(id))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to view this workspace");
        }
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workspace not found"));
        return workspaceMapper.toDTO(workspace);
    }

    public WorkspaceDTO update(String id, WorkspaceDTO request) {
        Workspace ws = workspaceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workspace not found"));

        ws.setName(request.getName());
        return workspaceMapper.toDTO(workspaceRepository.save(ws));
    }

    public void delete(String id) {
        if(workspaceRepository.findByUserNid(currentUserService.getCurrentUserNid()).stream().noneMatch(ws -> ws.getId().equals(id))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete this workspace");
        }
        if (!workspaceRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Workspace does not exist");
        }
        workspaceRepository.deleteById(id);
    }
}