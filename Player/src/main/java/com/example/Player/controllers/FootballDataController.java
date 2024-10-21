// src/main/java/com/example/Player/controllers/FootballDataController.java

package com.example.Player.controllers;

import com.example.Player.DTO.LeagueDTO;
import com.example.Player.DTO.MatchDTO;
import com.example.Player.DTO.TeamDTO;
import com.example.Player.mapper.LeagueMapper;
import com.example.Player.models.League;
import com.example.Player.services.FootballDataService;
import com.example.Player.services.StandingService;
import com.example.Player.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Charset", "utf-8");

        try {
            Optional<League> leagueOpt = footballDataService.getLeagueByCode(leagueCode);
            if (leagueOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "League not found"));
            }
            League league = leagueOpt.get();
            List<TeamDTO> teamDTOs = league.getTeams().stream()
                    .map(LeagueMapper.INSTANCE::teamToTeamDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(teamDTOs, headers, HttpStatus.OK);
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

    @GetMapping("/league/{leagueCode}/positions")
    public ResponseEntity<?> getLeaguePositions(@PathVariable String leagueCode) {
        try {
            Map<String, Object> positions = getQualificationPositionsByLeagueCode(leagueCode);
            return ResponseEntity.ok(positions);
        } catch (IllegalArgumentException e) {
            logger.error("Error fetching positions for league {}: {}", leagueCode, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error fetching positions for league {}: {}", leagueCode, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred."));
        }
    }

    // TODO -> This is hardcoded for now, in future, we will implement a JSON or XML file to get the requirements easily
    private Map<String, Object> getQualificationPositionsByLeagueCode(String leagueCode) {
        switch (leagueCode) {
            case "en.1":  // Premier League (England)
                return Map.of(
                        "Champions League", List.of("1st", "2nd", "3rd", "4th"),
                        "Europa League", List.of("5th", "FA Cup winner"),
                        "Conference League", List.of("Carabao Cup winner"),
                        "Relegation", List.of("18th", "19th", "20th")
                );
            case "es.1":  // La Liga (Spain)
                return Map.of(
                        "Champions League", List.of("1st", "2nd", "3rd", "4th"),
                        "Europa League", List.of("5th", "Copa del Rey winner"),
                        "Conference League", List.of("6th (or 7th if Copa del Rey winner qualifies for Europe)"),
                        "Relegation", List.of("18th", "19th", "20th")
                );
            case "de.1":  // Bundesliga (Germany)
                return Map.of(
                        "Champions League", List.of("1st", "2nd", "3rd", "4th"),
                        "Europa League", List.of("5th", "DFB-Pokal winner"),
                        "Conference League", List.of("6th (or 7th if DFB-Pokal winner qualifies for CL/EL)"),
                        "Relegation", List.of("16th (playoff)", "17th", "18th")
                );
            case "it.1":  // Serie A (Italy)
                return Map.of(
                        "Champions League", List.of("1st", "2nd", "3rd", "4th"),
                        "Europa League", List.of("5th", "Coppa Italia winner"),
                        "Conference League", List.of("6th (or 7th if Coppa Italia winner qualifies for Europe)"),
                        "Relegation", List.of("18th", "19th", "20th")
                );
            case "fr.1":  // Ligue 1 (France)
                return Map.of(
                        "Champions League", List.of("1st", "2nd (group stage)", "3rd (playoff)"),
                        "Europa League", List.of("4th", "Coupe de France winner"),
                        "Conference League", List.of("5th (or 6th if Coupe de France winner qualifies for Europe)"),
                        "Relegation", List.of("17th", "18th")
                );
            default:
                throw new IllegalArgumentException("Unsupported league code: " + leagueCode);
        }
    }

}
