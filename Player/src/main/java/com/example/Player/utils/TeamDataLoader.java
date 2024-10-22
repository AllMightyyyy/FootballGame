package com.example.Player.utils;

import com.example.Player.models.League;
import com.example.Player.models.Team;
import com.example.Player.services.LeagueService;
import com.example.Player.services.TeamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
public class TeamDataLoader {

    @Autowired
    private TeamService teamService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LeagueService leagueService;

    public void loadTeams(String fileName, String leagueName) throws IOException {
        // Construct the path to the JSON file within resources
        String resourcePath = "JsonData/2024-25/" + fileName;
        System.out.println("Attempting to load file: " + resourcePath);

        // Load the resource from the classpath
        Resource resource = new ClassPathResource(resourcePath);
        if (!resource.exists()) {
            throw new IOException("Resource not found: " + resourcePath);
        }

        // Get InputStream to read the file
        InputStream inputStream = resource.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        // Assume the JSON is an array of team names
        String[] teamNames = objectMapper.readValue(reader, String[].class);

        // Fetch or create the League
        League league = leagueService.getLeagueByName(leagueName)
                .orElseGet(() -> leagueService.saveLeague(new League(leagueName)));

        for (String teamName : teamNames) {
            teamService.createTeamIfNotExists(teamName, league);
        }
    }
}
