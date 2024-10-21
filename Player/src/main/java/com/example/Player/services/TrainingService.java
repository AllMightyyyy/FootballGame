package com.example.Player.services;

import com.example.Player.models.CoachType;
import com.example.Player.models.FantasyPlayer;
import com.example.Player.models.FantasyTeam;
import com.example.Player.models.TrainingSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TrainingService {

    @Autowired
    private TrainingSessionRepository trainingSessionRepository;

    @Autowired
    private FantasyPlayerService fantasyPlayerService;

    @Autowired
    private FantasyTeamService fantasyTeamService;

    public void startTraining(FantasyTeam fantasyTeam, Long fantasyPlayerId, CoachType coachType) throws Exception {
        FantasyPlayer fantasyPlayer = fantasyPlayerService.getFantasyPlayer(fantasyPlayerId)
                .orElseThrow(() -> new Exception("Fantasy Player not found."));

        if (!fantasyPlayer.getFantasyTeam().equals(fantasyTeam)) {
            throw new Exception("Player does not belong to your team.");
        }

        // Check if the player is already in a training session
        boolean isAlreadyTraining = trainingSessionRepository.findAll().stream()
                .anyMatch(session -> session.getFantasyPlayer().equals(fantasyPlayer) && session.getEndTime().isAfter(LocalDateTime.now()));
        if (isAlreadyTraining) {
            throw new Exception("Player is already in a training session.");
        }

        // Check if the team has available coaches
        // Implement logic to track coach availability

        // Create training session
        TrainingSession trainingSession = new TrainingSession();
        trainingSession.setFantasyTeam(fantasyTeam);
        trainingSession.setFantasyPlayer(fantasyPlayer);
        trainingSession.setCoachType(coachType);
        trainingSession.setStartTime(LocalDateTime.now());
        trainingSession.setEndTime(LocalDateTime.now().plusHours(8)); // 8-hour training

        trainingSessionRepository.save(trainingSession);
    }

    public void processCompletedTraining() {
        LocalDateTime now = LocalDateTime.now();
        List<TrainingSession> completedSessions = trainingSessionRepository.findByEndTimeBefore(now);

        for (TrainingSession session : completedSessions) {
            FantasyPlayer player = session.getFantasyPlayer();
            // Enhance player stats based on coach type
            enhancePlayerStats(player, session.getCoachType());

            // Decrease player stamina
            player.setStamina(Math.max(0, player.getStamina() - 10.0)); // Example stamina decrease
            fantasyPlayerService.saveFantasyPlayer(player);

            // Handle potential injuries
            if (Math.random() < calculateInjuryProbability(player)) {
                player.setInjured(true);
                fantasyPlayerService.saveFantasyPlayer(player);
            }

            // Remove the completed session
            trainingSessionRepository.delete(session);
        }
    }

    private void enhancePlayerStats(FantasyPlayer player, CoachType coachType) {
        // Implement logic to enhance player stats based on coach type
        Player realPlayer = player.getRealPlayer();
        switch (coachType) {
            case DEFENDING:
                realPlayer.setDefending(realPlayer.getDefending() + 1);
                break;
            case GOALKEEPING:
                realPlayer.setGoalkeeping(realPlayer.getGoalkeeping() + 1);
                break;
            case ATTACKING:
                realPlayer.setShooting(realPlayer.getShooting() + 1);
                break;
            case MIDFIELD:
                realPlayer.setPassing(realPlayer.getPassing() + 1);
                break;
        }
        // Save the updated real player if necessary
    }

    private double calculateInjuryProbability(FantasyPlayer player) {
        // Implement algorithm based on stamina and form
        if (player.getStamina() < 50) {
            return 0.05; // 5% chance
        }
        return 0.01; // 1% chance
    }
}
