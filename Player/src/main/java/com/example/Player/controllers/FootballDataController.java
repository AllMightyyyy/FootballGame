// src/main/java/com/example/Player/controllers/FootballDataController.java

package com.example.Player.controllers;

import com.example.Player.DTO.LeagueDTO;
import com.example.Player.DTO.MatchDTO;
import com.example.Player.DTO.TeamDTO;
import com.example.Player.models.League;
import com.example.Player.services.FootballDataService;
import com.example.Player.services.StandingService;
import com.example.Player.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/football")
public class FootballDataController {
    private final FootballDataService footballDataService;
    private static final Logger logger = LoggerFactory.getLogger(FootballDataController.class);
    @Autowired
    private StandingService standingService;
    @Autowired
    public FootballDataController(FootballDataService footballDataService) {
        this.footballDataService = footballDataService;
    }

    @GetMapping("/league/{leagueCode}")
    public ResponseEntity<?> getLeagueData(@PathVariable String leagueCode) {
        try {
            League league = footballDataService.getLeagueDataByCode(leagueCode);
            if (league == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "League not found with code: " + leagueCode));
            }

            LeagueDTO dto = LeagueMapper.INSTANCE.leagueToLeagueDTO(league);
            dto.setStandings(standingService.calculateStandings(league));

            // Map matches to MatchDTOs
            List<MatchDTO> matchDTOs = league.getMatches().stream()
                    .map(LeagueMapper.INSTANCE::matchToMatchDTO)
                    .collect(Collectors.toList());
            dto.setMatches(matchDTOs);

            return ResponseEntity.ok(dto);
        } catch (IOException e) {
            logger.error("IO error while fetching league data for {}: {}", leagueCode, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to load league data."));
        } catch (IllegalArgumentException e) {
            logger.error("Invalid league code {}: {}", leagueCode, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error while fetching league data for {}: {}", leagueCode, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred."));
        }
    }

    @GetMapping("/teams/{leagueCode}")
    public ResponseEntity<?> getTeams(@PathVariable String leagueCode) {
        try {
            Optional<League> leagueOpt = footballDataService.getLeagueByCode(leagueCode);
            if (leagueOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "League not found"));
            }
            League league = leagueOpt.get();
            List<TeamDTO> teamDTOs = league.getTeams().stream()
                    .map(LeagueMapper.INSTANCE::teamToTeamDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(teamDTOs);
        } catch (Exception e) {
            logger.error("Error fetching teams for league {}: {}", leagueCode, e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "An unexpected error occurred."));
        }
    }

    @GetMapping("/standings/{leagueCode}")
    public ResponseEntity<?> getStandings(@PathVariable String leagueCode) {
        try {
            LeagueDTO leagueDTO = footballDataService.getStandingsDTOByCode(leagueCode);
            return ResponseEntity.ok(leagueDTO);
        } catch (IllegalArgumentException e) {
            logger.error("Error fetching standings for league {}: {}", leagueCode, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error fetching standings for league {}: {}", leagueCode, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred."));
        }
    }
}
