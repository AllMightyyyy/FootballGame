// src/main/java/com/example/Player/services/MatchService.java

package com.example.Player.services;

import com.example.Player.models.League;
import com.example.Player.models.Match;
import com.example.Player.models.Team;
import com.example.Player.repository.MatchRepository;
import com.example.Player.utils.StandingDTO;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import org.slf4j.Logger;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private StandingService standingService; // Inject StandingService instead of FootballDataService

    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);

    @Transactional
    public void persistMatches(List<Match> matches, League league) {
        int savedCount = 0;
        int skippedCount = 0;
        for (Match match : matches) {
            boolean exists = matchRepository.existsByLeagueAndDateAndTimeAndTeam1AndTeam2(
                    league, match.getDate(), match.getTime(), match.getTeam1(), match.getTeam2()
            );
            if (!exists) {
                matchRepository.save(match);
                savedCount++;
                logger.debug("Saved new match: {} vs {} on {} at {} for League '{}'",
                        match.getTeam1().getName(),
                        match.getTeam2().getName(),
                        match.getDate(),
                        match.getTime(),
                        league.getName());
            } else {
                skippedCount++;
                logger.debug("Skipped existing match: {} vs {} on {} at {} for League '{}'",
                        match.getTeam1().getName(),
                        match.getTeam2().getName(),
                        match.getDate(),
                        match.getTime(),
                        league.getName());
            }
        }
        logger.info("Persisted matches for League '{}': {} saved, {} skipped.", league.getName(), savedCount, skippedCount);

        // Recalculate standings after saving matches
        List<StandingDTO> standings = standingService.calculateStandings(league);
        // Persist or update standings as needed
        // For example:
        //standingRepository.saveAll(convertToEntities(standings));
    }
}
