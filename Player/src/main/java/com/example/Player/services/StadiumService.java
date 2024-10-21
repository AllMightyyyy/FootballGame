package com.example.Player.services;

import com.example.Player.models.FantasyTeam;
import com.example.Player.models.Stadium;
import com.example.Player.repository.StadiumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StadiumService {

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private FantasyTeamService fantasyTeamService;

    public Stadium upgradeStadium(FantasyTeam fantasyTeam, UpgradeType upgradeType) throws Exception {
        Stadium stadium = stadiumRepository.findByFantasyTeam(fantasyTeam)
                .orElseThrow(() -> new Exception("Stadium not found for this Fantasy Team."));

        // Check upgrade levels and costs
        switch (upgradeType) {
            case CAPACITY:
                if (stadium.getCapacityLevel() >= 3) {
                    throw new Exception("Capacity already at maximum level.");
                }
                double capacityCost = calculateUpgradeCost(upgradeType, stadium.getCapacityLevel());
                if (fantasyTeam.getBalance() < capacityCost) {
                    throw new Exception("Insufficient balance for Capacity upgrade.");
                }
                stadium.setCapacity(stadium.getCapacity() + 1000); // Example increment
                stadium.setCapacityLevel(stadium.getCapacityLevel() + 1);
                fantasyTeam.setBalance(fantasyTeam.getBalance() - capacityCost);
                break;
            case PITCH:
                if (stadium.getPitchLevel() >= 3) {
                    throw new Exception("Pitch Quality already at maximum level.");
                }
                double pitchCost = calculateUpgradeCost(upgradeType, stadium.getPitchLevel());
                if (fantasyTeam.getBalance() < pitchCost) {
                    throw new Exception("Insufficient balance for Pitch Quality upgrade.");
                }
                stadium.setPitchQuality(stadium.getPitchQuality() + 0.1); // Example increment
                stadium.setPitchLevel(stadium.getPitchLevel() + 1);
                fantasyTeam.setBalance(fantasyTeam.getBalance() - pitchCost);
                break;
            case TRAINING:
                if (stadium.getTrainingLevel() >= 3) {
                    throw new Exception("Training Facilities already at maximum level.");
                }
                double trainingCost = calculateUpgradeCost(upgradeType, stadium.getTrainingLevel());
                if (fantasyTeam.getBalance() < trainingCost) {
                    throw new Exception("Insufficient balance for Training Facilities upgrade.");
                }
                stadium.setTrainingFacilities(stadium.getTrainingFacilities() + 0.1); // Example increment
                stadium.setTrainingLevel(stadium.getTrainingLevel() + 1);
                fantasyTeam.setBalance(fantasyTeam.getBalance() - trainingCost);
                break;
        }

        stadiumRepository.save(stadium);
        fantasyTeamService.saveFantasyTeam(fantasyTeam);
        return stadium;
    }

    private double calculateUpgradeCost(UpgradeType upgradeType, int currentLevel) {
        // Define costs for each upgrade type and level
        switch (upgradeType) {
            case CAPACITY:
                return 100_000.0 * (currentLevel + 1);
            case PITCH:
                return 150_000.0 * (currentLevel + 1);
            case TRAINING:
                return 200_000.0 * (currentLevel + 1);
            default:
                return 0.0;
        }
    }
}
