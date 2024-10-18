// src/main/java/com/example/Player/repository/LeagueRepository.java

package com.example.Player.repository;

import com.example.Player.models.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {
    Optional<League> findByName(String name);
    boolean existsByName(String name);
}
