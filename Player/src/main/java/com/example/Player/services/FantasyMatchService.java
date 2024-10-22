// FantasyMatchService.java
package com.example.Player.services;

import com.example.Player.models.FantasyMatch;
import com.example.Player.models.FantasyTeam;
import com.example.Player.repository.FantasyMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FantasyMatchService {

    @Autowired
    private FantasyMatchRepository fantasyMatchRepository;

    // Existing methods...

    /**
     * Retrieves all matches scheduled for today.
     */
    public List<FantasyMatch> getScheduledMatchesForToday() {
        LocalDate today = LocalDate.now();
        return fantasyMatchRepository.findByScheduledDate(today);
    }

    /**
     * Simulates a fantasy match between two teams.
     */
    public FantasyMatch simulateFantasyMatch(FantasyTeam team1, FantasyTeam team2) {
        FantasyMatch match = new FantasyMatch();
        match.setTeam1(team1);
        match.setTeam2(team2);
        match.setScheduledDate(LocalDate.now()); // Assuming today
        match.setStatus("scheduled");
        // Additional initialization as needed
        return fantasyMatchRepository.save(match);
    }

    /**
     * Saves the fantasy match.
     */
    public FantasyMatch saveFantasyMatch(FantasyMatch match) {
        return fantasyMatchRepository.save(match);
    }
}
