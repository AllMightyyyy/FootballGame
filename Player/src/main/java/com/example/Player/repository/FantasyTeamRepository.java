package com.example.Player.repository;

import com.example.Player.models.FantasyLeague;
import com.example.Player.models.FantasyTeam;
import com.example.Player.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FantasyTeamRepository extends JpaRepository<FantasyTeam, Long> {
    Optional<FantasyTeam> findByOwner(User user);
    boolean existsByFantasyLeagueAndTeamName(FantasyLeague fantasyLeague, String teamName);
}
