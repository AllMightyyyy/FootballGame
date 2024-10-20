// src/main/java/com/example/Player/utils/JsonDataLoader.java

package com.example.Player.utils;

import com.example.Player.DTO.LeagueDTO;
import com.example.Player.DTO.ScoreDTO;
import com.example.Player.models.League;
import com.example.Player.models.Match;
import com.example.Player.models.Score;
import com.example.Player.models.Team;
import com.example.Player.services.LeagueService;
import com.example.Player.services.MatchService;
import com.example.Player.services.TeamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JsonDataLoader {

    private static final Logger logger = LoggerFactory.getLogger(JsonDataLoader.class);

    @Autowired
    private TeamService teamService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private LeagueConfig leagueConfig;

    @Autowired
    private Validator validator;

    @PostConstruct
    public void init() {
        // Trigger data loading on application startup
        loadAllLeagues();
    }

    @Transactional
    public void loadAllLeagues() {
        leagueConfig.getLeagues().forEach((code, details) -> {
            try {
                loadLeagueData(code + ".json");
            } catch (IOException e) {
                logger.error("Failed to load league data for {}: {}", code, e.getMessage());
            } catch (IllegalArgumentException e) {
                logger.error("Invalid league configuration for {}: {}", code, e.getMessage());
            } catch (Exception e) {
                logger.error("An unexpected error occurred while processing {}: {}", code, e.getMessage());
            }
        });
    }

    @Transactional
    public League loadLeagueData(String fileName) throws IOException {
        try {
            String resourcePath = "JsonData/2024-25/" + fileName;
            logger.info("Attempting to load JSON file: {}", resourcePath);

            Resource resource = new ClassPathResource(resourcePath);
            if (!resource.exists()) {
                throw new IOException("Resource not found: " + resourcePath);
            }

            // Ensure the input stream is read using UTF-8 encoding
            InputStream inputStream = resource.getInputStream();
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8); // Use UTF-8 encoding

            // Use ObjectMapper to read the JSON file with the reader
            LeagueDTO leagueDTO = objectMapper.readValue(reader, LeagueDTO.class);

            String leagueCode = fileName.replace(".json", "");
            LeagueConfig.LeagueDetails leagueDetails = leagueConfig.getLeagues().get(leagueCode);

            if (leagueDetails == null) {
                throw new IllegalArgumentException("League details not found for code: " + leagueCode);
            }

            String name = leagueDetails.getName();
            String season = leagueDetails.getSeason();

            Optional<League> existingLeagueOpt = leagueService.getLeagueByCodeAndSeason(leagueCode, season);
            League league = existingLeagueOpt.orElseGet(() -> {
                League newLeague = new League();
                newLeague.setName(name);
                newLeague.setCode(leagueCode);
                newLeague.setSeason(season);
                leagueService.saveLeague(newLeague);
                logger.info("Created new league: {} - {} Season: {}", name, leagueCode, season);
                return newLeague;
            });

            // **Step 1: Create All Teams**
            Set<String> teamNames = leagueDTO.getMatches().stream()
                    .flatMap(dto -> Stream.of(dto.getTeam1(), dto.getTeam2()))
                    .collect(Collectors.toSet());

            for (String teamName : teamNames) {
                getOrCreateTeam(teamName, league);
            }

            // **Step 2: Create All Matches**
            List<Match> matches = leagueDTO.getMatches().stream()
                    .map(dto -> {
                        Match match = new Match();
                        match.setRound(dto.getRound());
                        match.setDate(dto.getDate());
                        match.setTime(dto.getTime());

                        Team team1 = teamService.getTeamByName(dto.getTeam1(), league);
                        if (team1 == null) {
                            throw new IllegalArgumentException("Team not found: " + dto.getTeam1());
                        }

                        Team team2 = teamService.getTeamByName(dto.getTeam2(), league);
                        if (team2 == null) {
                            throw new IllegalArgumentException("Team not found: " + dto.getTeam2());
                        }

                        match.setTeam1(team1);
                        match.setTeam2(team2);
                        match.setStatus(dto.getStatus());

                        if ("played".equalsIgnoreCase(dto.getStatus())) {
                            ScoreDTO scoreDTO = dto.getScore();
                            if (scoreDTO != null) {
                                List<Integer> htScores = scoreDTO.getHt();
                                List<Integer> ftScores = scoreDTO.getFt();

                                if (htScores != null && htScores.size() >= 2 && ftScores != null && ftScores.size() >= 2) {
                                    Score score = new Score(htScores.get(0), htScores.get(1), ftScores.get(0), ftScores.get(1));
                                    match.setScore(score);
                                } else {
                                    logger.warn("Incomplete score data for match: {} vs {} on {} at {}. Skipping score assignment.",
                                            dto.getTeam1(), dto.getTeam2(), dto.getDate(), dto.getTime());
                                }
                            } else {
                                logger.warn("Score is null for played match: {} vs {} on {} at {}.",
                                        dto.getTeam1(), dto.getTeam2(), dto.getDate(), dto.getTime());
                            }
                        } else if ("scheduled".equalsIgnoreCase(dto.getStatus())) {
                            logger.info("Scheduled match: {} vs {} on {} at {}. No scores to assign.",
                                    dto.getTeam1(), dto.getTeam2(), dto.getDate(), dto.getTime());
                        } else {
                            logger.warn("Unknown status '{}' for match: {} vs {} on {} at {}.",
                                    dto.getStatus(), dto.getTeam1(), dto.getTeam2(), dto.getDate(), dto.getTime());
                        }

                        match.setLeague(league);
                        return match;
                    })
                    .collect(Collectors.toList());

            // **Step 3: Persist Matches**
            matchService.persistMatches(matches, league);
            logger.info("Persisted {} matches for League: {}", matches.size(), league.getName());

            return league;
        } catch (Exception e) {
            logger.error("Failed to load league data: {}", e.getMessage());
            throw e;
        }
    }

    private Team getOrCreateTeam(String teamName, League league) {
        Optional<Team> teamOpt = Optional.ofNullable(teamService.getTeamByName(teamName, league));
        Team team;
        if (teamOpt.isPresent()) {
            team = teamOpt.get();
            logger.debug("Found existing team: {}", team.getName());
        } else {
            team = new Team();
            team.setName(teamName);
            team.setLeague(league);
            teamService.saveTeam(team);
            logger.info("Created new team: {}", teamName);
        }
        return team;
    }


}
