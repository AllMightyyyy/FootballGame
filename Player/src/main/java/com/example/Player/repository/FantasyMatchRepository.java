package com.example.Player.repository;

import com.example.Player.models.FantasyMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FantasyMatchRepository extends JpaRepository<FantasyMatch, Long> {
    // Add any custom queries if needed
}
