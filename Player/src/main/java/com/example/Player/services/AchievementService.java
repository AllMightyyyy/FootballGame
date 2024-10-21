package com.example.Player.services;

import com.example.Player.models.Achievement;
import com.example.Player.models.FantasyTeam;
import com.example.Player.repository.AchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    public void assignAchievement(FantasyTeam fantasyTeam, Achievement achievement) {
        fantasyTeam.getAchievements().add(achievement);
        achievement.getFantasyTeams().add(fantasyTeam);
        achievementRepository.save(achievement);
        // Save fantasy team if necessary
    }

    // Additional methods as needed
}
