package com.example.Player.utils;

import com.example.Player.PlayerApplication;
import com.example.Player.models.League;
import com.example.Player.services.MatchService;
import com.example.Player.services.LeagueService;
import com.example.Player.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TeamDataLoaderRunner implements CommandLineRunner {

    @Autowired
    private JsonDataLoader jsonDataLoader;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private MatchService matchService;

    @Override
    public void run(String... args) throws Exception {
        String[] leagueFiles = {
                "EnglishPremierLeague.json",
                "ItalianSerieA.json",
                "FrenchLigue1.json",
                "SpainPrimeraDivision.json",
                "German1Bundesliga.json"
        };

        for (String fileName : leagueFiles) {
            try {
                League league = jsonDataLoader.loadLeagueData(fileName);
                matchService.persistMatches(league.getMatches(), league);
                System.out.println("Loaded and persisted data for league: " + league.getName());
            } catch (IOException e) {
                System.err.println("Failed to load teams for " + fileName + ": " + e.getMessage());
            } catch (Exception e) {
                System.err.println("An error occurred while processing " + fileName + ": " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        // Initialize Spring Context
        ConfigurableApplicationContext context = SpringApplication.run(PlayerApplication.class, args);

        // Retrieve JsonDataLoader bean
        JsonDataLoader jsonDataLoader = context.getBean(JsonDataLoader.class);

        // Specify the file you want to test
        String filePath = "EnglishPremierLeague.json";

        try {
            League league = jsonDataLoader.loadLeagueData(filePath);
            System.out.println(league);
        } catch (IOException e) {
            System.err.println("Error loading league data: " + e.getMessage());
        }

        // Close the context
        context.close();
    }

}
