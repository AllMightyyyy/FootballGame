package com.example.Player.utils;

import com.example.Player.models.League;
import com.example.Player.models.Match;
import com.example.Player.models.Score;
import com.example.Player.models.Team;
import com.example.Player.services.MatchService;
import com.example.Player.services.TeamService;
import com.example.Player.services.LeagueService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JsonDataLoader {

    @Autowired
    private TeamService teamService; // Service to fetch Team entities

    @Autowired
    private ObjectMapper objectMapper; // Jackson ObjectMapper

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private MatchService matchService;

    public League loadLeagueData(String fileName) throws IOException {
        // Configure ObjectMapper to ignore unknown properties
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Construct the path to the JSON file within resources
        String resourcePath = "JsonData/2024-25/" + fileName;

        // Load the resource from the classpath
        Resource resource = new ClassPathResource(resourcePath);
        if (!resource.exists()) {
            throw new IOException("Resource not found: " + resourcePath);
        }

        // Get InputStream to read the file
        InputStream inputStream = resource.getInputStream();

        // Deserialize JSON into LeagueDTO
        LeagueDTO leagueDTO = objectMapper.readValue(inputStream, LeagueDTO.class);

        // Create or fetch the League entity
        League league = leagueService.getLeagueByName(leagueDTO.getName())
                .orElseGet(() -> leagueService.saveLeague(new League(leagueDTO.getName())));

        // Map MatchDTOs to Match entities with Team associations
        List<Match> matches = leagueDTO.getMatches().stream().map(dto -> {
            Match match = new Match();
            match.setRound(dto.getRound());
            match.setDate(dto.getDate());
            match.setTime(dto.getTime());

            // Fetch Team1 from the database using the League
            Team team1 = teamService.getTeamByName(dto.getTeam1(), league);
            if (team1 == null) {
                // Create the team if it doesn't exist
                teamService.createTeamIfNotExists(dto.getTeam1(), league);
            }
            match.setTeam1(team1);

            // Fetch Team2 from the database using the League
            Team team2 = teamService.getTeamByName(dto.getTeam2(), league);
            if (team2 == null) {
                // Create the team if it doesn't exist
                teamService.createTeamIfNotExists(dto.getTeam2(), league);
            }
            match.setTeam2(team2);

            // Map scores (convert List<Integer> to int[])
            int[] htArray = dto.getScore().getHt().stream().mapToInt(Integer::intValue).toArray();
            int[] ftArray = dto.getScore().getFt().stream().mapToInt(Integer::intValue).toArray();

            Score score = new Score();
            score.setHt(htArray);
            score.setFt(ftArray);
            match.setScore(score);

            // Associate Match with League
            match.setLeague(league);

            return match;
        }).collect(Collectors.toList());

        // Set Matches to League
        league.setMatches(matches);

        // Persist matches to the database
        // Assuming you have a MatchService to handle this
        matchService.persistMatches(matches, league);

        return league;
    }


}
