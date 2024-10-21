// TeamManagementController.java
package com.example.Player.controllers;

import com.example.Player.models.FantasyPlayer;
import com.example.Player.models.FantasyTeam;
import com.example.Player.models.User;
import com.example.Player.services.FantasyPlayerService;
import com.example.Player.services.FantasyTeamService;
import com.example.Player.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/team")
public class TeamManagementController {

    @Autowired
    private FantasyTeamService fantasyTeamService;

    @Autowired
    private UserService userService;

    @Autowired
    private FantasyPlayerService fantasyPlayerService;

    /**
     * Get Team Details
     */
    @GetMapping("/details")
    public ResponseEntity<?> getTeamDetails(@RequestParam Long userId) {
        try {
            User user = userService.findById(userId)
                    .orElseThrow(() -> new Exception("User not found."));
            FantasyTeam fantasyTeam = fantasyTeamService.getFantasyTeamByUser(user)
                    .orElseThrow(() -> new Exception("Fantasy Team not found for user."));
            return ResponseEntity.ok(fantasyTeam);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Assign a Player to the Team
     */
    @PostMapping("/assign-player")
    public ResponseEntity<?> assignPlayerToTeam(@RequestParam Long userId,
                                                @RequestParam Long playerId) {
        try {
            User user = userService.findById(userId)
                    .orElseThrow(() -> new Exception("User not found."));
            FantasyTeam fantasyTeam = fantasyTeamService.getFantasyTeamByUser(user)
                    .orElseThrow(() -> new Exception("Fantasy Team not found for user."));
            FantasyPlayer fantasyPlayer = fantasyPlayerService.getFantasyPlayer(playerId)
                    .orElseThrow(() -> new Exception("Fantasy Player not found."));
            fantasyTeamService.assignPlayerToTeam(fantasyTeam, fantasyPlayer);
            return ResponseEntity.ok(Map.of("message", "Player assigned to team successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Assign a Specialist Role to a Player
     */
    @PostMapping("/assign-specialist")
    public ResponseEntity<?> assignSpecialist(@RequestParam Long userId,
                                              @RequestParam Long playerId,
                                              @RequestParam String specialistType) {
        try {
            User user = userService.findById(userId)
                    .orElseThrow(() -> new Exception("User not found."));
            FantasyTeam fantasyTeam = fantasyTeamService.getFantasyTeamByUser(user)
                    .orElseThrow(() -> new Exception("Fantasy Team not found for user."));
            FantasyPlayer fantasyPlayer = fantasyPlayerService.getFantasyPlayer(playerId)
                    .orElseThrow(() -> new Exception("Fantasy Player not found."));

            // Assign specialist based on specialistType
            switch (specialistType.toUpperCase()) {
                case "PENALTY_TAKER":
                    fantasyPlayer.setPenaltyTaker(true);
                    break;
                case "CORNER_TAKER":
                    fantasyPlayer.setCornerTaker(true);
                    break;
                case "FREEKICK_TAKER":
                    fantasyPlayer.setFreeKickTaker(true);
                    break;
                default:
                    throw new Exception("Invalid specialist type.");
            }

            fantasyPlayerService.saveFantasyPlayer(fantasyPlayer);
            return ResponseEntity.ok(Map.of("message", specialistType + " assigned to player successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Change Formation
     */
    @PostMapping("/change-formation")
    public ResponseEntity<?> changeFormation(@RequestParam Long userId,
                                             @RequestParam String newFormation) {
        try {
            User user = userService.findById(userId)
                    .orElseThrow(() -> new Exception("User not found."));
            FantasyTeam fantasyTeam = fantasyTeamService.getFantasyTeamByUser(user)
                    .orElseThrow(() -> new Exception("Fantasy Team not found for user."));
            fantasyTeamService.changeFormation(fantasyTeam, newFormation);
            return ResponseEntity.ok(Map.of("message", "Formation changed to " + newFormation + " successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Substitute Players
     */
    @PostMapping("/substitute")
    public ResponseEntity<?> substitutePlayers(@RequestParam Long userId,
                                               @RequestParam Long outPlayerId,
                                               @RequestParam Long inPlayerId) {
        try {
            User user = userService.findById(userId)
                    .orElseThrow(() -> new Exception("User not found."));
            FantasyTeam fantasyTeam = fantasyTeamService.getFantasyTeamByUser(user)
                    .orElseThrow(() -> new Exception("Fantasy Team not found for user."));
            fantasyTeamService.substitutePlayers(fantasyTeam, outPlayerId, inPlayerId);
            return ResponseEntity.ok(Map.of("message", "Player substitution successful."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Additional endpoints like removing specialists, viewing lineups, etc., can be added here
}
