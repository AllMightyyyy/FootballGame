package com.example.Player.controllers;

import com.example.Player.models.FantasyTeam;
import com.example.Player.models.FriendlyMatch;
import com.example.Player.models.User;
import com.example.Player.services.FantasyTeamService;
import com.example.Player.services.FriendlyMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/friendly-match")
public class FriendlyMatchController {

    @Autowired
    private FriendlyMatchService friendlyMatchService;

    @Autowired
    private FantasyTeamService fantasyTeamService;

    @PostMapping("/request")
    public ResponseEntity<?> requestFriendlyMatch(@AuthenticationPrincipal User user,
                                                  @RequestParam Long targetTeamId) {
        try {
            FantasyTeam requesterTeam = fantasyTeamService.getFantasyTeamByUser(user)
                    .orElseThrow(() -> new Exception("Fantasy Team not found for user."));

            FantasyTeam targetTeam = fantasyTeamService.getFantasyTeamById(targetTeamId)
                    .orElseThrow(() -> new Exception("Target Fantasy Team not found."));

            FriendlyMatch friendlyMatch = friendlyMatchService.requestFriendlyMatch(requesterTeam, targetTeam);
            return ResponseEntity.ok(friendlyMatch);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/accept")
    public ResponseEntity<?> acceptFriendlyMatch(@RequestParam Long matchId) {
        try {
            friendlyMatchService.acceptFriendlyMatch(matchId);
            return ResponseEntity.ok(Map.of("message", "Friendly Match accepted and scheduled."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/simulate")
    public ResponseEntity<?> simulateFriendlyMatch(@RequestParam Long matchId) {
        try {
            friendlyMatchService.simulateFriendlyMatch(matchId);
            return ResponseEntity.ok(Map.of("message", "Friendly Match simulated successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Additional endpoints like decline, view matches, etc.
}
