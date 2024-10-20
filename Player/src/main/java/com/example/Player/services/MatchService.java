// src/main/java/com/example/Player/services/MatchService.java

package com.example.Player.services;

import com.example.Player.models.League;
import com.example.Player.models.Match;
import com.example.Player.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class MatchService {

    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private MatchPersistenceService matchPersistenceService;

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
}
