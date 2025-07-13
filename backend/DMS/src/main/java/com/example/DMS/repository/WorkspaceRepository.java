package com.example.DMS.repository;

import com.example.DMS.models.Workspace;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WorkspaceRepository extends MongoRepository<Workspace, String> {
    List<Workspace> findByUserNid(String userNid);
}
