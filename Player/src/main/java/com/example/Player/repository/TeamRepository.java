// src/main/java/com/example/Player/repository/TeamRepository.java
package com.example.Player.repository;

import com.example.Player.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByNameAndLeague(String name, String league);
    boolean existsByNameAndLeague(String name, String league);
}
