// AchievementService.java
package com.example.Player.services;

import com.example.Player.models.Achievement;
import com.example.Player.models.FantasyTeam;
import com.example.Player.repository.AchievementRepository;
import com.example.Player.repository.FantasyTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private FantasyTeamRepository fantasyTeamRepository;

    /**
     * Assigns an achievement to a fantasy team.
     */
    public void assignAchievement(FantasyTeam fantasyTeam, Achievement achievement) {
        fantasyTeam.getAchievements().add(achievement);
        achievement.getFantasyTeams().add(fantasyTeam);
        achievementRepository.save(achievement);
        fantasyTeamRepository.save(fantasyTeam); // Ensure FantasyTeam is saved if necessary
    }

    // Additional methods as needed
}
