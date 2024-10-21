package com.example.Player.repository;

import com.example.Player.models.FantasyLeague;
import com.example.Player.models.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FantasyLeagueRepository extends JpaRepository<FantasyLeague, Long> {
    Optional<FantasyLeague> findByRealLeague(League realLeague);
}
