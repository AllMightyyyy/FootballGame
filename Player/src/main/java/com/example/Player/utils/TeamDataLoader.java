package com.example.Player.services;

import com.example.Player.models.League;
import com.example.Player.models.Team;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.IOException;

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

        // Assume the JSON is an array of team names
        String[] teamNames = objectMapper.readValue(inputStream, String[].class);

        // Fetch or create the League
        League league = leagueService.getLeagueByName(leagueName)
                .orElseGet(() -> leagueService.saveLeague(new League(leagueName)));

        for (String teamName : teamNames) {
            teamService.createTeamIfNotExists(teamName, league);
        }
    }
}
