// FantasyPlayerService.java
package com.example.Player.services;

import com.example.Player.models.FantasyLeague;
import com.example.Player.models.FantasyPlayer;
import com.example.Player.repository.FantasyPlayerRepository;
import com.example.Player.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FantasyPlayerService {

    @Autowired
    private FantasyPlayerRepository fantasyPlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public FantasyPlayer saveFantasyPlayer(FantasyPlayer fantasyPlayer) {
        return fantasyPlayerRepository.save(fantasyPlayer);
    }

    public List<FantasyPlayer> getFreeAgents(FantasyLeague fantasyLeague) {
        return fantasyPlayerRepository.findByFantasyLeagueAndFantasyTeamIsNull(fantasyLeague);
    }

    public Optional<FantasyPlayer> getFantasyPlayer(Long id) {
        return fantasyPlayerRepository.findById(id);
    }

    public void updateStaminaAndInjuries() {
        List<FantasyPlayer> players = fantasyPlayerRepository.findAll();
        for (FantasyPlayer fantasyPlayer : players) {
            // Decrease stamina based on games played, training intensity, age
            double staminaDecrease = calculateStaminaDecrease(fantasyPlayer);
            fantasyPlayer.setStamina(Math.max(0, fantasyPlayer.getStamina() - staminaDecrease));

            // Determine injury
            if (shouldPlayerGetInjured(fantasyPlayer)) {
                fantasyPlayer.setInjured(true);
            }

            fantasyPlayerRepository.save(fantasyPlayer);
        }
    }

    private double calculateStaminaDecrease(FantasyPlayer fantasyPlayer) {
        // Implement logic based on games played, training intensity, age
        // Placeholder logic
        return 5.0;
    }

    private boolean shouldPlayerGetInjured(FantasyPlayer fantasyPlayer) {
        // Implement injury probability algorithm
        // Placeholder logic: higher stamina and form reduce injury chance
        double injuryProbability = 0.01; // 1% base probability
        if (fantasyPlayer.getStamina() < 50) {
            injuryProbability += 0.02; // Increase by 2%
        }
        // Random chance
        return Math.random() < injuryProbability;
    }

    // Additional methods for specialists, spying, etc.
}
