package com.example.Player.services;

import com.example.Player.DTO.FantasyMatchSimulationRequest;
import com.example.Player.DTO.MatchOutcome;
import com.example.Player.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MatchSimulationService {

    @Autowired
    private FantasyMatchService fantasyMatchService;

    @Autowired
    private FantasyTeamService fantasyTeamService;

    @Autowired
    private RefereeService refereeService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private FantasyPlayerService fantasyPlayerService;

    @Autowired
    private FantasyLeagueService fantasyLeagueService;

    public void simulateMatch(FantasyMatchSimulationRequest request) throws Exception {
        FantasyLeague fantasyLeague = fantasyLeagueService.getFantasyLeagueByRealLeague(request.getRealLeague())
                .orElseThrow(() -> new Exception("Fantasy League not found."));

        FantasyTeam team1 = fantasyTeamService.getFantasyTeamByUser(request.getUser1())
                .orElseThrow(() -> new Exception("Fantasy Team not found for User1."));

        FantasyTeam team2 = fantasyTeamService.getFantasyTeamByUser(request.getUser2())
                .orElseThrow(() -> new Exception("Fantasy Team not found for User2."));

        // Fetch lineups and tactics
        Lineup lineup1 = team1.getLineup();
        Lineup lineup2 = team2.getLineup();
        Tactics tactics1 = request.getTactics1();
        Tactics tactics2 = request.getTactics2();

        // Assign a referee
        Referee referee = refereeService.assignRefereeToMatch();

        // Calculate team strengths
        double strength1 = calculateTeamStrength(team1, lineup1, tactics1);
        double strength2 = calculateTeamStrength(team2, lineup2, tactics2);

        // Simulate goals and events
        MatchOutcome outcome = simulateEvents(strength1, strength2, tactics1, tactics2, referee);

        // Update team balances and objectives
        updatePostMatch(team1, team2, outcome);

        // Record match result
        FantasyMatch match = fantasyMatchService.simulateFantasyMatch(team1, team2);
        match.setTeam1Score(outcome.getTeam1Score());
        match.setTeam2Score(outcome.getTeam2Score());
        match.setStatus("completed");
        fantasyMatchService.saveFantasyMatch(match);
    }

    private double calculateTeamStrength(FantasyTeam team, Lineup lineup, Tactics tactics) {
        double strength = 0.0;
        for (FantasyPlayer player : lineup.getStarting11()) {
            strength += player.getRealPlayer().getOverall() * (player.getStamina() / 100.0);
        }
        // Adjust based on formation and tactics
        // Example: possession game increases passing and control
        if (tactics.getGamePlan().equalsIgnoreCase("POSSESSION")) {
            strength *= 1.1;
        } else if (tactics.getGamePlan().equalsIgnoreCase("LONG_BALL")) {
            strength *= 0.9;
        }

        // Factor in pressing intensity
        strength += tactics.getPressing() * 10;

        return strength;
    }

    private MatchOutcome simulateEvents(double strength1, double strength2, Tactics tactics1, Tactics tactics2, Referee referee) {
        // Implement detailed simulation logic
        // Placeholder: Simple goal calculation
        Random random = new Random();
        int team1Goals = (int) ((strength1 / (strength1 + strength2)) * 5 + random.nextInt(3));
        int team2Goals = (int) ((strength2 / (strength1 + strength2)) * 5 + random.nextInt(3));

        return new MatchOutcome(team1Goals, team2Goals);
    }

    private void updatePostMatch(FantasyTeam team1, FantasyTeam team2, MatchOutcome outcome) {
        if (outcome.getTeam1Score() > outcome.getTeam2Score()) {
            team1.setBalance(team1.getBalance() + 10000); // Reward for win
            team2.setBalance(team2.getBalance() - 5000); // Penalty for loss
            // Update objectives
        } else if (outcome.getTeam1Score() < outcome.getTeam2Score()) {
            team2.setBalance(team2.getBalance() + 10000);
            team1.setBalance(team1.getBalance() - 5000);
        } else {
            team1.setBalance(team1.getBalance() + 5000);
            team2.setBalance(team2.getBalance() + 5000);
        }

        fantasyTeamService.saveFantasyTeam(team1);
        fantasyTeamService.saveFantasyTeam(team2);
    }
}
