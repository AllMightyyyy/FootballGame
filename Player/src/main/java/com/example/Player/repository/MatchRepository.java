// src/main/java/com/example/Player/repository/MatchRepository.java

package com.example.Player.repository;

import com.example.Player.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    // Additional query methods can be defined here if needed
}
