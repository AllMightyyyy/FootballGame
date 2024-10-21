// src/main/java/com/example/Player/services/MatchService.java

package com.example.Player.services;

import com.example.Player.models.League;
import com.example.Player.models.Match;
import com.example.Player.models.Score;
import com.example.Player.models.Team;
import com.example.Player.repository.MatchRepository;
import com.example.Player.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MatchService {

    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private MatchPersistenceService matchPersistenceService;

    @Autowired
    private TeamService teamService;

    /**
     * Persists a list of Match entities, handling each in a separate transaction.
     */
    @Transactional
    public void persistMatches(List<Match> matches, League league) {
        int savedCount = 0;
        int skippedCount = 0;

        for (Match match : matches) {
            try {
                boolean exists = matchRepository.existsByLeagueAndDateAndTimeAndTeam1AndTeam2(
                        league, match.getDate(), match.getTime(), match.getTeam1(), match.getTeam2()
                );

                if (!exists) {
                    matchPersistenceService.saveMatch(match, league);
                    savedCount++;
                } else {
                    skippedCount++;
                    logger.debug("Skipped existing match: {} vs {} on {} at {} for League '{}'",
                            match.getTeam1().getName(),
                            match.getTeam2().getName(),
                            match.getDate(),
                            match.getTime(),
                            league.getName());
                }
            } catch (Exception e) {
                logger.error("Failed to save match: {} vs {} on {} at {} for League '{}'. Error: {}",
                        match.getTeam1().getName(),
                        match.getTeam2().getName(),
                        match.getDate(),
                        match.getTime(),
                        league.getName(),
                        e.getMessage());
                // Continue with the next match
            }
        }

        logger.info("Persisted matches for League '{}': {} saved, {} skipped.", league.getName(), savedCount, skippedCount);
    }

    // Fetch form of the last 5 matches
    /**
     * Get the last 5 matches and compute form for the team.
     */
    public List<String> getTeamForm(String teamName) {
        // Find the team by name
        Optional<Team> teamOpt = teamService.getTeamByName(teamName);
        if (teamOpt.isEmpty()) {
            throw new IllegalArgumentException("Team not found: " + teamName);
        }

        Team team = teamOpt.get();
        League league = team.getLeague();

        // Get the last 5 matches played by the team
        Pageable pageable = PageRequest.of(0, 5);
        List<Match> lastMatches = matchRepository.findLastMatchesByTeam(team.getId(), league.getId(), pageable);

        // Calculate form based on the match results
        List<String> form = new ArrayList<>();
        for (Match match : lastMatches) {
            boolean isTeam1 = match.getTeam1().getId().equals(team.getId());
            int teamGoals = isTeam1 ? match.getScore().getFtTeam1() : match.getScore().getFtTeam2();
            int opponentGoals = isTeam1 ? match.getScore().getFtTeam2() : match.getScore().getFtTeam1();

            if (teamGoals > opponentGoals) {
                form.add("W");  // Win
            } else if (teamGoals < opponentGoals) {
                form.add("L");  // Loss
            } else {
                form.add("D");  // Draw
            }
        }

        return form;
    }

    /**
     * Get the next scheduled match for the team.
     */
    public Match getNextMatchForTeam(String teamName) {
        // Find the team by name
        Optional<Team> teamOpt = teamService.getTeamByName(teamName);
        if (teamOpt.isEmpty()) {
            throw new IllegalArgumentException("Team not found: " + teamName);
        }

        Team team = teamOpt.get();

        // Find the next match for the team
        Pageable pageable = PageRequest.of(0, 1);  // Limit to only the next match
        Page<Match> page = matchRepository.findNextMatchForTeamByName(team.getName(), pageable);

        if (page.hasContent()) {
            return page.getContent().get(0);  // Return the first match
        } else {
            throw new IllegalArgumentException("No upcoming matches found for team: " + teamName);
        }
    }

}
