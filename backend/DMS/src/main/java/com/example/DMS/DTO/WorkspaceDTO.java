package com.example.DMS.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceDTO {
    @NotBlank(message = "Workspace name is required")
    private String name;

    private String userNid;

    private String id;

    public String getName() {
        return name;
    }

    public String getUserNid() {
        return userNid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserNid(String userNid) {
        this.userNid = userNid;
    }
}
