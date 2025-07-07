package com.example.DMS.repository;


import com.example.DMS.models.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {}, type = EntityGraph.EntityGraphType.FETCH)

    Optional<User> findByEmail(String email);
}
