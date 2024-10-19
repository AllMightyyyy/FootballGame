// src/main/java/com/example/Player/repository/MatchRepository.java

package com.example.Player.repository;

import com.example.Player.models.League;
import com.example.Player.models.Match;
import com.example.Player.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByLeagueId(Long leagueId);

    boolean existsByLeagueAndDateAndTimeAndTeam1AndTeam2(
            League league, String date, String time, Team team1, Team team2
    );
}
