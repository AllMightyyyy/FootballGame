// FantasyMatchRepository.java
package com.example.Player.repository;

import com.example.Player.models.FantasyMatch;
import com.example.Player.models.FantasyTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FantasyMatchRepository extends JpaRepository<FantasyMatch, Long> {
    List<FantasyMatch> findByStatus(String status);
    // Additional query methods as needed
}
