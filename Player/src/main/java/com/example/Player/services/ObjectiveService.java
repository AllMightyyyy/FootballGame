package com.example.Player.services;

import com.example.Player.models.FantasyTeam;
import com.example.Player.models.Objective;
import com.example.Player.repository.ObjectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ObjectiveService {

    @Autowired
    private ObjectiveRepository objectiveRepository;

    public void assignObjective(FantasyTeam fantasyTeam, String description) {
        Objective objective = new Objective();
        objective.setDescription(description);
        objective.setFantasyTeam(fantasyTeam);
        objective.setAchieved(false);
        objectiveRepository.save(objective);
    }

    public void updateObjectiveStatus(FantasyTeam fantasyTeam, Objective objective, boolean status) {
        objective.setAchieved(status);
        objectiveRepository.save(objective);
        // Check for achievements based on objectives
    }

    // Additional methods as needed
}
