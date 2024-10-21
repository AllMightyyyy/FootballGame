package com.example.Player.services;

import com.example.Player.models.FantasyMatch;
import com.example.Player.models.FantasyPlayer;
import com.example.Player.models.FantasyTeam;
import com.example.Player.models.Player;
import com.example.Player.repository.FantasyMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FantasyMatchService {

    @Autowired
    private FantasyMatchRepository fantasyMatchRepository;

    public FantasyMatch simulateFantasyMatch(FantasyTeam team1, FantasyTeam team2) {
        int team1Score = calculateTeamScore(team1);
        int team2Score = calculateTeamScore(team2);

        FantasyMatch match = new FantasyMatch();
        match.setTeam1(team1);
        match.setTeam2(team2);
        match.setTeam1Score(team1Score);
        match.setTeam2Score(team2Score);
        match.setStatus("completed");

        return fantasyMatchRepository.save(match);
    }

    private int calculateTeamScore(FantasyTeam team) {
        int totalScore = 0;
        for (FantasyPlayer player : team.getPlayers()) {
            totalScore += calculatePlayerScore(player);
        }
        return totalScore;
    }

    private int calculatePlayerScore(FantasyPlayer player) {
        // Example fantasy score calculation based on real-life player performance
        Player realPlayer = player.getRealPlayer();
        return realPlayer.getOverall();  // Simple formula, can be expanded
    }
}

