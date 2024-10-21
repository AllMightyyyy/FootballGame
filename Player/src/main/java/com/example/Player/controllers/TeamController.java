// src/main/java/com/example/Player/controllers/TeamController.java

package com.example.Player.controllers;

import com.example.Player.models.League;
import com.example.Player.models.Match;
import com.example.Player.models.Team;
import com.example.Player.models.User;
import com.example.Player.services.LeagueService;
import com.example.Player.services.MatchService;
import com.example.Player.services.TeamService;
import com.example.Player.services.UserService;
import com.example.Player.DTO.TeamLeagueResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = "http://localhost:3000") // Adjust as needed
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private MatchService matchService;

    // Get all leagues (return codes and names)
    @GetMapping("/all-leagues")
    public ResponseEntity<List<TeamLeagueResponseDTO>> getLeagues() {
        List<League> leagues = leagueService.getAllLeagues();
        List<TeamLeagueResponseDTO> response = leagues.stream()
                .map(league -> new TeamLeagueResponseDTO(league.getCode(), league.getName(), league.getSeason()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // Get teams by league code
    @GetMapping("/by-league/{leagueCode}")
    public ResponseEntity<List<TeamResponse>> getTeamsByLeague(@PathVariable String leagueCode) {
        Optional<League> leagueOpt = leagueService.getCurrentLeagueByCode(leagueCode);
        if (leagueOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<Team> teams = teamService.getTeamsByLeague(leagueOpt.get());
        if (teams.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<TeamResponse> response = teams.stream()
                .map(team -> new TeamResponse(team.getId(), team.getName(), team.getUser() != null))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{leagueCode}/{season}")
    public ResponseEntity<List<TeamResponse>> getTeamsByLeagueAndSeason(@PathVariable String leagueCode, @PathVariable String season) {
        Optional<League> leagueOpt = leagueService.getLeagueByCodeAndSeason(leagueCode, season);
        if (leagueOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<Team> teams = teamService.getTeamsByLeague(leagueOpt.get());
        if (teams.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<TeamResponse> response = teams.stream()
                .map(team -> new TeamResponse(team.getId(), team.getName(), team.getUser() != null))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // Assign team to user
    @PostMapping("/assign")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> assignTeamToUser(@RequestBody AssignTeamRequest request, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not found"));
        }
        User user = userOpt.get();

        if (user.getTeam() != null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User already has a team assigned"));
        }

        boolean success = teamService.assignTeamToUser(request.getLeagueCode(), request.getTeamName(), user);
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not found"));
        }
        User user = userOpt.get();
        Optional<Team> teamOpt = teamService.getUserTeam(user);
        if (teamOpt.isPresent()) {
            Team team = teamOpt.get();
            return ResponseEntity.ok(Map.of(
                    "id", team.getId(),
                    "name", team.getName(),
                    "leagueCode", team.getLeague().getCode(),
                    "leagueName", team.getLeague().getName(),
                    "season", team.getLeague().getSeason()
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                    "message", "No team assigned"
            ));
        }
    }

    @GetMapping("/{teamName}/form")
    public ResponseEntity<?> getTeamForm(@PathVariable String teamName) {
        try {
            List<String> form = matchService.getTeamForm(teamName);
            return ResponseEntity.ok(Map.of("form", form));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{teamName}/next-opponent")
    public ResponseEntity<?> getNextOpponent(@PathVariable String teamName) {
        try {
            Match nextMatch = matchService.getNextMatchForTeam(teamName);
            String nextOpponent = nextMatch.getTeam1().getName().equals(teamName)
                    ? nextMatch.getTeam2().getName()
                    : nextMatch.getTeam1().getName();
            return ResponseEntity.ok(Map.of("nextOpponent", nextOpponent));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    // DTOs for requests and responses
    static class AssignTeamRequest {
        private String leagueCode;
        private String teamName;

        // Getters and Setters
        public String getLeagueCode() {
            return leagueCode;
        }

        public void setLeagueCode(String leagueCode) {
            this.leagueCode = leagueCode;
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

        // Getters and Setters
        public Long getId() { return id; }
        public String getName() { return name; }
        public boolean isOccupied() { return isOccupied; }

        public void setId(Long id) { this.id = id; }
        public void setName(String name) { this.name = name; }
        public void setOccupied(boolean occupied) { isOccupied = occupied; }
    }

    static class LeagueResponse {
        private String code;
        private String name;
        private String season;

        public LeagueResponse(String code, String name, String season) {
            this.code = code;
            this.name = name;
            this.season = season;
        }

        // Getters and Setters
        public String getCode() { return code; }
        public String getName() { return name; }
        public String getSeason() { return season; }

        public void setCode(String code) { this.code = code; }
        public void setName(String name) { this.name = name; }
        public void setSeason(String season) { this.season = season; }
    }
}
