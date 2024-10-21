package com.example.Player.repository;

import com.example.Player.models.FantasyTeam;
import com.example.Player.models.FriendlyMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendlyMatchRepository extends JpaRepository<FriendlyMatch, Long> {
    List<FriendlyMatch> findByTeam1OrTeam2(FantasyTeam team1, FantasyTeam team2);
}
