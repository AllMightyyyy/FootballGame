package com.example.Player.services;

import com.example.Player.models.Player;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FormationService {

    // A simple map to represent the 4-3-3 formation
    private final Map<String, Player> formation = new HashMap<>();

    public FormationService() {
        initializeFormation();
    }

    private void initializeFormation() {
        formation.put("GK", null);  // Goalkeeper
        formation.put("LB", null);  // Left Back
        formation.put("CB1", null); // Center Back 1
        formation.put("CB2", null); // Center Back 2
        formation.put("RB", null);  // Right Back
        formation.put("CM1", null); // Center Midfielder 1
        formation.put("CM2", null); // Center Midfielder 2
        formation.put("CM3", null); // Center Midfielder 3
        formation.put("LW", null);  // Left Wing
        formation.put("RW", null);  // Right Wing
        formation.put("ST", null);  // Striker
    }

    // Add a player to the formation if they can play the position
    public boolean addPlayerToPosition(String position, Player player) {
        if (formation.containsKey(position) && player.getPositionsList().contains(position)) {
            formation.put(position, player);
            return true;
        }
        return false;
    }

    // Remove a player from the formation
    public boolean removePlayerFromPosition(String position) {
        if (formation.containsKey(position)) {
            formation.put(position, null);
            return true;
        }
        return false;
    }

    // Get the current formation setup
    public Map<String, Player> getFormation() {
        return formation;
    }
}
