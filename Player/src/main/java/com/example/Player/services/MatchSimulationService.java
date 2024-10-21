// MatchSimulationService.java
package com.example.Player.services;

import com.example.Player.DTO.FantasyMatchSimulationRequest;
import com.example.Player.DTO.MatchOutcome;
import com.example.Player.models.*;
import com.example.Player.repository.FantasyMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private ObjectiveService objectiveService;

    private Random random = new Random();

    /**
     * Simulates a match based on the provided request.
     */
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
        Referee referee = request.getReferee();

        // Calculate team strengths
        double strength1 = calculateTeamStrength(team1, lineup1, tactics1);
        double strength2 = calculateTeamStrength(team2, lineup2, tactics2);

        // Simulate goals and events
        MatchOutcome outcome = simulateEvents(strength1, strength2, tactics1, tactics2, referee, lineup1, lineup2);

        // Update team balances and objectives
        updatePostMatch(team1, team2, outcome);

        // Record match result
        FantasyMatch match = fantasyMatchService.simulateFantasyMatch(team1, team2);
        match.setTeam1Score(outcome.getTeam1Score());
        match.setTeam2Score(outcome.getTeam2Score());
        match.setStatus("completed");
        match.setEvents(String.join(", ", outcome.getEvents()));
        fantasyMatchService.saveFantasyMatch(match);
    }

    /**
     * Calculates the overall strength of a team based on lineup, tactics, and player stats.
     */
    private double calculateTeamStrength(FantasyTeam team, Lineup lineup, Tactics tactics) {
        double strength = 0.0;
        for (FantasyPlayer player : lineup.getStarting11()) {
            // Base strength from player overall and stamina
            strength += player.getRealPlayer().getOverall() * (player.getStamina() / 100.0);

            // Add specialist bonuses
            if (player.isPenaltyTaker()) {
                strength += 2.0;
            }
            if (player.isCornerTaker()) {
                strength += 1.5;
            }
            if (player.isFreeKickTaker()) {
                strength += 1.5;
            }
        }

        // Adjust based on formation
        strength *= formationMultiplier(tactics.getFormation());

        // Factor in pressing intensity
        strength += tactics.getPressing() * 0.05; // Example impact

        // Factor in style
        strength += tactics.getStyleValue();

        // Factor in marking type
        strength += tactics.getMarkingTypeValue();

        // Factor in offside trap
        if (tactics.isOffsideTrap()) {
            strength += 1.0;
        }

        return strength;
    }

    /**
     * Multiplier based on formation.
     */
    private double formationMultiplier(String formation) {
        switch (formation) {
            case "4-3-3":
                return 1.1;
            case "3-5-2":
                return 1.0;
            case "4-4-2":
                return 1.05;
            default:
                return 1.0;
        }
    }

    /**
     * Simulates match events based on team strengths and tactics.
     */
    private MatchOutcome simulateEvents(double strength1, double strength2, Tactics tactics1, Tactics tactics2, Referee referee, Lineup lineup1, Lineup lineup2) {
        MatchOutcome outcome = new MatchOutcome(0, 0);
        List<String> events = new ArrayList<>();

        // Calculate goal probabilities
        double totalStrength = strength1 + strength2;
        double goalProbability1 = strength1 / totalStrength;
        double goalProbability2 = strength2 / totalStrength;

        // Simulate goals (e.g., total goals can be between 0 and 5)
        int totalGoals = random.nextInt(6); // 0 to 5 goals

        for (int i = 0; i < totalGoals; i++) {
            if (random.nextDouble() < goalProbability1) {
                outcome.setTeam1Score(outcome.getTeam1Score() + 1);
                String scorer = selectGoalScorer(tactics1, lineup1);
                events.add("Team1 scored by " + scorer);
            } else {
                outcome.setTeam2Score(outcome.getTeam2Score() + 1);
                String scorer = selectGoalScorer(tactics2, lineup2);
                events.add("Team2 scored by " + scorer);
            }
        }

        // Simulate cards based on tackling intensity and referee leniency
        simulateCards(outcome, tactics1, tactics2, referee, events);

        // Simulate possession and turnovers
        simulatePossession(outcome, tactics1, tactics2, events);

        // Assign events to outcome
        outcome.getEvents().addAll(events);

        return outcome;
    }

    /**
     * Selects a goal scorer based on player stats and specialties.
     */
    private String selectGoalScorer(Tactics tactics, Lineup lineup) {
        List<FantasyPlayer> potentialScorers = lineup.getStarting11().stream()
                .filter(player -> player.isFreeKickTaker() || player.isPenaltyTaker() || player.isCornerTaker()
                        || player.getRealPlayer().getShooting() > 80) // Example criteria
                .collect(Collectors.toList());

        if (potentialScorers.isEmpty()) {
            // If no specialists, choose a random attacker
            potentialScorers = lineup.getStarting11().stream()
                    .filter(player -> player.getRealPlayer().getShooting() > 70)
                    .collect(Collectors.toList());
        }

        if (potentialScorers.isEmpty()) {
            // As a fallback, choose any player
            potentialScorers = lineup.getStarting11();
        }

        FantasyPlayer scorer = potentialScorers.get(random.nextInt(potentialScorers.size()));
        return scorer.getRealPlayer().getLongName();
    }

    /**
     * Simulates card events based on tackling intensity and referee leniency.
     */
    private void simulateCards(MatchOutcome outcome, Tactics tactics1, Tactics tactics2, Referee referee, List<String> events) {
        // Define base probabilities
        double yellowCardProb = 0.05; // 5% base chance per tackling action
        double redCardProb = 0.01;    // 1% base chance per tackling action

        // Adjust based on tackling intensity
        yellowCardProb *= tactics1.getTacklingIntensityFactor();
        yellowCardProb *= tactics2.getTacklingIntensityFactor();
        redCardProb *= tactics1.getTacklingIntensityFactor();
        redCardProb *= tactics2.getTacklingIntensityFactor();

        // Adjust based on referee leniency
        yellowCardProb *= referee.getLeniencyFactor();
        redCardProb *= referee.getLeniencyFactor();

        // Simulate yellow cards
        if (random.nextDouble() < yellowCardProb) {
            String player = "Team1 Player" + (random.nextInt(11) + 1);
            events.add("Yellow Card to " + player);
        }
        if (random.nextDouble() < yellowCardProb) {
            String player = "Team2 Player" + (random.nextInt(11) + 1);
            events.add("Yellow Card to " + player);
        }

        // Simulate red cards
        if (random.nextDouble() < redCardProb) {
            String player = "Team1 Player" + (random.nextInt(11) + 1);
            events.add("Red Card to " + player);
        }
        if (random.nextDouble() < redCardProb) {
            String player = "Team2 Player" + (random.nextInt(11) + 1);
            events.add("Red Card to " + player);
        }
    }

    /**
     * Simulates possession changes and turnovers based on tactics.
     */
    private void simulatePossession(MatchOutcome outcome, Tactics tactics1, Tactics tactics2, List<String> events) {
        // Example: If a team presses high, they have a higher chance to win possession
        double possession1 = calculatePossession(tactics1);
        double possession2 = calculatePossession(tactics2);

        if (random.nextDouble() < possession1 / (possession1 + possession2)) {
            events.add("Team1 maintains possession.");
        } else {
            events.add("Team2 maintains possession.");
        }

        // Simulate turnovers leading to potential scoring opportunities
        if (random.nextDouble() < 0.3) { // 30% chance of turnover leading to chance
            events.add("Turnover by Team1 leading to Team2 opportunity.");
            // Potential to simulate a goal or assist
        }

        if (random.nextDouble() < 0.3) {
            events.add("Turnover by Team2 leading to Team1 opportunity.");
        }
    }

    /**
     * Calculates possession probability based on team tactics.
     */
    private double calculatePossession(Tactics tactics) {
        // Example logic: Pressing and possession game plans influence possession
        double basePossession = 50.0;
        basePossession += tactics.getPressing() * 0.3; // Higher pressing increases possession
        if (tactics.getGamePlan().equalsIgnoreCase("POSSESSION")) {
            basePossession += 10.0;
        } else if (tactics.getGamePlan().equalsIgnoreCase("LONG_BALL")) {
            basePossession -= 10.0;
        }
        // Clamp possession between 30 and 70 for balance
        return Math.max(30.0, Math.min(70.0, basePossession));
    }

    /**
     * Updates team balances and objectives based on match outcome.
     */
    private void updatePostMatch(FantasyTeam team1, FantasyTeam team2, MatchOutcome outcome) {
        if (outcome.getTeam1Score() > outcome.getTeam2Score()) {
            // Team1 wins
            team1.setBalance(team1.getBalance() + 10000); // Reward for win
            team2.setBalance(team2.getBalance() - 5000); // Penalty for loss
            // Update objectives
            updateObjectives(team1, "Win the Match");
            updateObjectives(team2, "Avoid Defeat");
        } else if (outcome.getTeam1Score() < outcome.getTeam2Score()) {
            // Team2 wins
            team2.setBalance(team2.getBalance() + 10000);
            team1.setBalance(team1.getBalance() - 5000);
            // Update objectives
            updateObjectives(team2, "Win the Match");
            updateObjectives(team1, "Avoid Defeat");
        } else {
            // Draw
            team1.setBalance(team1.getBalance() + 5000);
            team2.setBalance(team2.getBalance() + 5000);
            // Update objectives
            updateObjectives(team1, "Draw the Match");
            updateObjectives(team2, "Draw the Match");
        }

        fantasyTeamService.saveFantasyTeam(team1);
        fantasyTeamService.saveFantasyTeam(team2);
    }

    /**
     * Updates team objectives based on match results.
     */
    private void updateObjectives(FantasyTeam team, String objectiveDescription) {
        // Check if the team has an objective matching the description
        Optional<Objective> objectiveOpt = team.getObjectives().stream()
                .filter(obj -> obj.getDescription().equalsIgnoreCase(objectiveDescription) && !obj.isAchieved())
                .findFirst();

        if (objectiveOpt.isPresent()) {
            Objective objective = objectiveOpt.get();
            objective.setAchieved(true);
            // Possibly trigger achievements or rewards
            Achievement achievement = new Achievement("Objective Achieved: " + objectiveDescription);
            fantasyTeamService.assignAchievement(team, achievement);
        }
    }
}
