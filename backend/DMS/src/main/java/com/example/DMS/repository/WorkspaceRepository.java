package com.example.DMS.repository;

import com.example.DMS.models.Workspace;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WorkspaceRepository extends MongoRepository<Workspace, String> {
    List<Workspace> findByUserNid(String userNid);

    @Query(value = "{ '_id' : ?0 }", fields = "{ 'folders' : 1 }")
    Optional<Workspace> findByIdWithFolders(String id);
}
