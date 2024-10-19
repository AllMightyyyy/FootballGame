// src/main/java/com/example/Player/services/StandingService.java

package com.example.Player.services;

import com.example.Player.models.League;
import com.example.Player.models.Match;
import com.example.Player.models.Score;
import com.example.Player.models.Team;
import com.example.Player.utils.StandingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StandingService {

    private static final Logger logger = LoggerFactory.getLogger(StandingService.class);

    /**
     * Calculate Standings for a League
     */
    public List<StandingDTO> calculateStandings(League league) {
        List<StandingDTO> standings = new ArrayList<>();

        // Initialize standings for each team
        for (Team team : league.getTeams()) {
            standings.add(new StandingDTO(team.getName()));
        }

        int totalMatches = league.getMatches().size();
        int processedMatches = 0;

        // Process each match
        for (Match match : league.getMatches()) {
            Score score = match.getScore();
            if (score == null || score.getFtTeam1() == null || score.getFtTeam2() == null) {
                logger.debug("Skipping match: {} vs {} on {} at {} due to incomplete FT scores.",
                        match.getTeam1().getName(),
                        match.getTeam2().getName(),
                        match.getDate(),
                        match.getTime());
                continue; // Skip matches without complete FT scores
            }

            int team1Goals = score.getFtTeam1();
            int team2Goals = score.getFtTeam2();

            StandingDTO team1Standing = standings.stream()
                    .filter(s -> s.getTeamName().equals(match.getTeam1().getName()))
                    .findFirst().orElse(null);

            StandingDTO team2Standing = standings.stream()
                    .filter(s -> s.getTeamName().equals(match.getTeam2().getName()))
                    .findFirst().orElse(null);

            if (team1Standing == null || team2Standing == null) {
                logger.warn("Team not found in standings: {} vs {}.", match.getTeam1().getName(), match.getTeam2().getName());
                continue; // Team not found, skip
            }

            // Update played
            team1Standing.setPlayed(team1Standing.getPlayed() + 1);
            team2Standing.setPlayed(team2Standing.getPlayed() + 1);

            // Update goals
            team1Standing.setGoalsFor(team1Standing.getGoalsFor() + team1Goals);
            team1Standing.setGoalsAgainst(team1Standing.getGoalsAgainst() + team2Goals);
            team2Standing.setGoalsFor(team2Standing.getGoalsFor() + team2Goals);
            team2Standing.setGoalsAgainst(team2Standing.getGoalsAgainst() + team1Goals);

            // Update goal difference
            team1Standing.setGoalDifference(team1Standing.getGoalsFor() - team1Standing.getGoalsAgainst());
            team2Standing.setGoalDifference(team2Standing.getGoalsFor() - team2Standing.getGoalsAgainst());

            // Determine match outcome
            if (team1Goals > team2Goals) {
                // Team1 wins
                team1Standing.setWin(team1Standing.getWin() + 1);
                team1Standing.setPoints(team1Standing.getPoints() + 3);
                team2Standing.setLoss(team2Standing.getLoss() + 1);
            } else if (team1Goals < team2Goals) {
                // Team2 wins
                team2Standing.setWin(team2Standing.getWin() + 1);
                team2Standing.setPoints(team2Standing.getPoints() + 3);
                team1Standing.setLoss(team1Standing.getLoss() + 1);
            } else {
                // Draw
                team1Standing.setDraw(team1Standing.getDraw() + 1);
                team2Standing.setDraw(team2Standing.getDraw() + 1);
                team1Standing.setPoints(team1Standing.getPoints() + 1);
                team2Standing.setPoints(team2Standing.getPoints() + 1);
            }

            processedMatches++;
        }

        logger.info("League '{}': Total Matches in Database = {}, Matches Processed for Standings = {}",
                league.getName(), totalMatches, processedMatches);

        // Sort standings
        standings.sort(Comparator.comparingInt(StandingDTO::getPoints).reversed()
                .thenComparingInt(StandingDTO::getGoalDifference).reversed()
                .thenComparingInt(StandingDTO::getGoalsFor).reversed());

        logger.info("Standings calculated successfully for League: {}", league.getName());
        return standings;
    }
}
