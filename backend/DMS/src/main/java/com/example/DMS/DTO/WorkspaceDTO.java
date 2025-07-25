package com.example.DMS.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceDTO {
    @NotBlank(message = "Workspace name is required")
    private String name;

    private String userNid;

    private String id;

    private LocalDateTime createdAt;

    public String getName() {
        return name;
    }

    public String getUserNid() {
        return userNid;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserNid(String userNid) {
        this.userNid = userNid;
    }
}