// src/main/java/com/example/Player/repository/MatchRepository.java

package com.example.Player.repository;

import com.example.Player.models.League;
import com.example.Player.models.Match;
import com.example.Player.models.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByLeagueId(Long leagueId);

    boolean existsByLeagueAndDateAndTimeAndTeam1AndTeam2(
            League league, String date, String time, Team team1, Team team2
    );

    // Fetch last played matches for a team, ordered by date descending
    @Query("SELECT m FROM Match m WHERE (m.team1.id = :teamId OR m.team2.id = :teamId) " +
            "AND m.league.id = :leagueId AND m.score.ftTeam1 IS NOT NULL AND m.score.ftTeam2 IS NOT NULL " +
            "ORDER BY m.date DESC")
    List<Match> findLastMatchesByTeam(@Param("teamId") Long teamId, @Param("leagueId") Long leagueId, Pageable pageable);

    // Fetch the next scheduled match for a team
    @Query("SELECT m FROM Match m WHERE (m.team1.name = :teamName OR m.team2.name = :teamName) " +
            "AND m.status = 'scheduled' " +
            "ORDER BY m.date ASC, m.time ASC")
    Page<Match> findNextMatchForTeamByName(@Param("teamName") String teamName, Pageable pageable);

}
