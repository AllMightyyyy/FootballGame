// src/main/java/com/example/Player/controllers/TeamController.java

package com.example.Player.controllers;

import com.example.Player.models.League;
import com.example.Player.models.Team;
import com.example.Player.models.User;
import com.example.Player.services.TeamService;
import com.example.Player.services.UserService;
import com.example.Player.repository.LeagueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = "http://localhost:3000") // Adjust as needed
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Autowired
    private LeagueRepository leagueRepository;

    // Get all leagues
    @GetMapping("/leagues")
    public ResponseEntity<List<String>> getLeagues() {
        List<String> leagues = List.of(
                "English Premier League",
                "Spain Primera Division",
                "German 1. Bundesliga",
                "Italian Serie A",
                "French Ligue 1"
                // Remove "Spanish La Liga", "German Bundesliga", etc.
        );
        return ResponseEntity.ok(leagues);
    }

    // Get all teams in a specific league
    @GetMapping("/{league}")
    public ResponseEntity<List<TeamResponse>> getTeamsByLeague(@PathVariable String league) {
        List<Team> teams = teamService.getTeamsByLeague(league);
        if (teams.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<TeamResponse> response = teams.stream()
                .map(team -> new TeamResponse(team.getId(), team.getName(), team.getUser() != null))
                .toList();
        return ResponseEntity.ok(response);
    }

    // Assign a team to the authenticated user
    @PostMapping("/assign")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> assignTeamToUser(@RequestBody AssignTeamRequest request, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "User not found"));
        }
        User user = userOpt.get();

        if (user.getTeam() != null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User already has a team assigned"));
        }

        boolean success = teamService.assignTeamToUser(request.getLeague(), request.getTeamName(), user);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "Team assigned successfully"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Team is already occupied or does not exist"));
        }
    }

    // Get the authenticated user's team
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserTeam(Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "User not found"));
        }
        User user = userOpt.get();
        Optional<Team> teamOpt = teamService.getUserTeam(user);
        if (teamOpt.isPresent()) {
            Team team = teamOpt.get();
            return ResponseEntity.ok(Map.of(
                    "id", team.getId(),
                    "name", team.getName(),
                    "league", team.getLeague().getName()
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                    "message", "No team assigned"
            ));
        }
    }

    // DTOs for request and response
    static class AssignTeamRequest {
        private String league;
        private String teamName;

        public String getLeague() {
            return league;
        }

        public void setLeague(String league) {
            this.league = league;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }
    }

    static class TeamResponse {
        private Long id;
        private String name;
        private boolean isOccupied;

        public TeamResponse(Long id, String name, boolean isOccupied) {
            this.id = id;
            this.name = name;
            this.isOccupied = isOccupied;
        }

        // Getters and setters
        public Long getId() { return id; }
        public String getName() { return name; }
        public boolean isOccupied() { return isOccupied; }

        public void setId(Long id) { this.id = id; }
        public void setName(String name) { this.name = name; }
        public void setOccupied(boolean occupied) { isOccupied = occupied; }
    }
}
