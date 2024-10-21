package com.example.Player.repository;

import com.example.Player.models.FantasyLeague;
import com.example.Player.models.FantasyPlayer;
import com.example.Player.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FantasyPlayerRepository extends JpaRepository<FantasyPlayer, Long> {
    List<FantasyPlayer> findByFantasyLeagueAndFantasyTeamIsNull(FantasyLeague fantasyLeague); // Free agents
    Optional<FantasyPlayer> findByRealPlayerAndFantasyLeague(Player realPlayer, FantasyLeague fantasyLeague);
}
