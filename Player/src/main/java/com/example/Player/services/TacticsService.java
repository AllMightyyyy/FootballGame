// TacticsService.java
package com.example.Player.services;

import com.example.Player.models.MarkingType;
import com.example.Player.models.TacklingIntensity;
import com.example.Player.models.Tactics;
import org.springframework.stereotype.Service;

@Service
public class TacticsService {

    /**
     * Creates a new Tactics object with the provided parameters.
     */
    public Tactics createTactics(String gamePlan, TacklingIntensity tacklingIntensity, int pressing, String style, MarkingType markingType, boolean offsideTrap) {
        Tactics tactics = new Tactics();
        tactics.setGamePlan(gamePlan);
        tactics.setTacklingIntensity(tacklingIntensity);
        tactics.setPressing(pressing);
        tactics.setStyle(style);
        tactics.setMarkingType(markingType);
        tactics.setOffsideTrap(offsideTrap);
        return tactics;
    }

    // Additional methods to update or retrieve tactics can be added here
}
