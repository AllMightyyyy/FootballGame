// FantasyMatchService.java
package com.example.Player.services;

import com.example.Player.models.FantasyMatch;
import com.example.Player.models.FantasyTeam;
import com.example.Player.repository.FantasyMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FantasyMatchService {

    @Autowired
    private FantasyMatchRepository fantasyMatchRepository;

    public FantasyMatch simulateFantasyMatch(FantasyTeam team1, FantasyTeam team2) {
        FantasyMatch match = new FantasyMatch();
        match.setTeam1(team1);
        match.setTeam2(team2);
        match.setStatus("scheduled");
        match.setScheduledAt(LocalDateTime.now().plusDays(1)); // Example scheduling
        return fantasyMatchRepository.save(match);
    }

    public FantasyMatch saveFantasyMatch(FantasyMatch match) {
        return fantasyMatchRepository.save(match);
    }
}
