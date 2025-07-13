package com.example.DMS.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "workspaces")
public class Workspace {

    @Id
    private String id;

    private String name;

    private String userNid;

    private LocalDateTime createdAt = LocalDateTime.now();

    public String getUserNid() {
        return userNid;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserNid(String userNid) {
        this.userNid = userNid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }
}
