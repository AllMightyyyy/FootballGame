// src/main/java/com/example/Player/utils/JsonDataLoader.java

package com.example.Player.utils;

import com.example.Player.exceptions.GlobalExceptionHandler;
import com.example.Player.models.League;
import com.example.Player.models.Match;
import com.example.Player.models.Score;
import com.example.Player.models.Team;
import com.example.Player.services.LeagueService;
import com.example.Player.services.MatchService;
import com.example.Player.services.TeamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JsonDataLoader {

    private static final Logger logger = LoggerFactory.getLogger(JsonDataLoader.class);

    @Autowired
    private TeamService teamService; // Service to fetch Team entities

    @Autowired
    private ObjectMapper objectMapper; // Jackson ObjectMapper

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private LeagueConfig leagueConfig;

    @Transactional
    public League loadLeagueData(String fileName) throws IOException {
        try {
            // Construct the path to the JSON file within resources
            String resourcePath = "JsonData/2024-25/" + fileName;
            logger.info("Attempting to load JSON file: {}", resourcePath);

            // Load the resource from the classpath
            Resource resource = new ClassPathResource(resourcePath);
            if (!resource.exists()) {
                String errorMsg = "Resource not found: " + resourcePath;
                logger.error(errorMsg);
                throw new IOException(errorMsg);
            }

            // Get InputStream to read the file
            InputStream inputStream = resource.getInputStream();

            // Deserialize JSON into LeagueDTO
            LeagueDTO leagueDTO = objectMapper.readValue(inputStream, LeagueDTO.class);
            logger.info("Parsed LeagueDTO: Name={}, Number of Matches={}", leagueDTO.getName(), leagueDTO.getMatches().size());

            // Extract league code from filename, e.g., "en.1.json" -> "en.1"
            String leagueCode = fileName.replace(".json", "");
            logger.info("Extracted League Code: {}", leagueCode);

            // Fetch LeagueDetails from LeagueConfig
            LeagueConfig.LeagueDetails leagueDetails = leagueConfig.getLeagues().get(leagueCode);
            if (leagueDetails == null) {
                String errorMsg = "LeagueDetails not found for code: " + leagueCode;
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }

            String name = leagueDetails.getName();
            String season = leagueDetails.getSeason();

            // Create or fetch the League entity with code and season
            Optional<League> existingLeagueOpt = leagueService.getLeagueByCodeAndSeason(leagueCode, season);
            final League league;
            if (existingLeagueOpt.isPresent()) {
                league = existingLeagueOpt.get();
                logger.info("Fetched existing League: ID={}, Name={}, Code={}, Season={}",
                        league.getId(), league.getName(), league.getCode(), league.getSeason());
            } else {
                league = new League(name, leagueCode, season);
                leagueService.saveLeague(league);
                logger.info("Created new League: ID={}, Name={}, Code={}, Season={}",
                        league.getId(), league.getName(), league.getCode(), league.getSeason());
            }

            // Map MatchDTOs to Match entities with Team associations
            List<Match> matches = leagueDTO.getMatches().stream().map(dto -> {
                Match match = new Match();
                match.setRound(dto.getRound());
                match.setDate(dto.getDate());
                match.setTime(dto.getTime());

                // Fetch or create Team1 and Team2 using helper method
                final Team team1 = getOrCreateTeam(dto.getTeam1(), league);
                match.setTeam1(team1);

                final Team team2 = getOrCreateTeam(dto.getTeam2(), league);
                match.setTeam2(team2);

                // Set the score (Only FT scores are considered)
                if (dto.getScore() != null && dto.getScore().getFt() != null && dto.getScore().getFt().size() == 2) {
                    Integer ftTeam1 = dto.getScore().getFt().get(0);
                    Integer ftTeam2 = dto.getScore().getFt().get(1);
                    Integer htTeam1 = (dto.getScore().getHt() != null && dto.getScore().getHt().size() == 2) ? dto.getScore().getHt().get(0) : null;
                    Integer htTeam2 = (dto.getScore().getHt() != null && dto.getScore().getHt().size() == 2) ? dto.getScore().getHt().get(1) : null;
                    Score score = new Score(htTeam1, htTeam2, ftTeam1, ftTeam2);
                    match.setScore(score);
                } else {
                    logger.warn("Incomplete or missing FT scores for match: {} vs {}. Setting score to null.", dto.getTeam1(), dto.getTeam2());
                    match.setScore(null);
                }

                // Associate Match with League
                match.setLeague(league);

                logger.info("Constructed Match: Round={}, Team1={}, Team2={}, FT Score: {}-{}",
                        match.getRound(),
                        team1.getName(),
                        team2.getName(),
                        match.getScore() != null ? match.getScore().getFtTeam1() : "N/A",
                        match.getScore() != null ? match.getScore().getFtTeam2() : "N/A"
                );

                return match;
            }).collect(Collectors.toList());

            // Persist matches using MatchService to handle duplicates
            matchService.persistMatches(matches, league);
            logger.info("Persisted {} matches for League: {}", matches.size(), league.getName());
            return league;
        } catch (Exception e) {
            logger.error("Specific error: {}", e.getMessage());
            throw e; // Rethrow to let Spring handle transaction rollback
        }


    }
    /**
     * Helper method to extract season from resource path
     * Example: "JsonData/2024-25/league.json" -> "2024-25"
     */
    private String extractSeasonFromResourcePath(String resourcePath) {
        String[] parts = resourcePath.split("/");
        for (String part : parts) {
            if (part.matches("\\d{4}-\\d{2}")) {
                return part;
            }
        }
        return "Unknown";
    }

    /**
     * Helper method to fetch or create a Team by name within a League
     */
    private Team getOrCreateTeam(String teamName, League league) {
        Team team = teamService.getTeamByName(teamName, league);
        if (team == null) {
            team = teamService.createTeamIfNotExists(teamName, league);
            logger.info("Created new team: Name={}", teamName);
        } else {
            logger.info("Fetched existing team: Name={}", teamName);
        }
        return team;
    }
}
