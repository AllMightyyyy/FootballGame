package com.example.Player.controllers;

import com.example.Player.DTO.FantasyTeamChoiceRequest;
import com.example.Player.models.FantasyLeague;
import com.example.Player.models.FantasyTeam;
import com.example.Player.models.User;
import com.example.Player.services.FantasyLeagueService;
import com.example.Player.services.FantasyTeamService;
import com.example.Player.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/fantasy-team")
public class FantasyTeamController {

    @Autowired
    private FantasyTeamService fantasyTeamService;

    @Autowired
    private UserService userService;

    @Autowired
    private FantasyLeagueService fantasyLeagueService;

    @PostMapping("/choose")
    public ResponseEntity<?> chooseFantasyTeam(@AuthenticationPrincipal User user,
                                               @RequestBody FantasyTeamChoiceRequest request) {
        try {
            FantasyLeague fantasyLeague = fantasyLeagueService.getFantasyLeagueByRealLeague(request.getRealLeague())
                    .orElseThrow(() -> new Exception("Fantasy League not found for the given Real League."));

            FantasyTeam fantasyTeam = fantasyTeamService.chooseFantasyTeam(user, request.getTeamName(), fantasyLeague);
            return ResponseEntity.ok(fantasyTeam);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Additional endpoints like viewing team details, managing lineup, etc.
}
