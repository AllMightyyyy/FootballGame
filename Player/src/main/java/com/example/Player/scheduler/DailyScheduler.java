package com.example.Player.scheduler;

import com.example.Player.services.FantasyPlayerService;
import com.example.Player.services.MatchSimulationService;
import com.example.Player.services.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyScheduler {

    @Autowired
    private FantasyPlayerService fantasyPlayerService;

    @Autowired
    private MatchSimulationService matchSimulationService;

    @Autowired
    private TrainingService trainingService;

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void performDailyOperations() throws Exception {
        // Update player stamina and handle injuries
        fantasyPlayerService.updateStaminaAndInjuries();

        // Simulate scheduled matches
        matchSimulationService.simulateScheduledMatches();

        // Process ongoing training sessions
        trainingService.processTrainingSessions();
    }
}
