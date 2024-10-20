// src/main/java/com/example/Player/services/MatchPersistenceService.java

package com.example.Player.services;

import com.example.Player.models.League;
import com.example.Player.models.Match;
import com.example.Player.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MatchPersistenceService {

    private static final Logger logger = LoggerFactory.getLogger(MatchPersistenceService.class);

    @Autowired
    private MatchRepository matchRepository;

    /**
     * Persists a single Match entity in a new transaction.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveMatch(Match match, League league) {
        matchRepository.save(match);
        logger.debug("Saved match: {} vs {} on {} at {} for League '{}'",
                match.getTeam1().getName(),
                match.getTeam2().getName(),
                match.getDate(),
                match.getTime(),
                league.getName());
    }
}
