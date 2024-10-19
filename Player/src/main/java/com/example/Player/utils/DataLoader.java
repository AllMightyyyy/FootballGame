// src/main/java/com/example/Player/utils/DataLoader.java

package com.example.Player.utils;

import com.example.Player.models.League;
import com.example.Player.services.FootballDataService;
import com.example.Player.services.PlayerService;
import com.example.Player.services.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private FootballDataService footballDataService;

    @Autowired
    private PlayerService playerService;

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        // Create default user
        String defaultUsername = "devuser";
        String defaultEmail = "devuser@example.com";
        String defaultPassword = "devpassword";

        if (userService.isUsernameAvailable(defaultUsername)) {
            userService.registerUser(defaultUsername, defaultEmail, defaultPassword);
            logger.info("Default user 'devuser' created.");
        } else {
            logger.info("Default user 'devuser' already exists.");
        }

        // Load and persist data for all leagues
        String[] leagueNames = {
                "English Premier League 2024/25",
                "Italian Serie A 2024/25",
                "French Ligue 1 2024/25",
                "Primera División de España 2024/25",
                "Deutsche Bundesliga 2024/25"
        };

        for (String leagueName : leagueNames) {
            try {
                // Check if the league already exists based on name and season
                boolean leagueExists = footballDataService.leagueExists(leagueName);
                if (leagueExists) {
                    logger.info("League '{}' already exists. Skipping data loading.", leagueName);
                    continue;
                }

                // Load league data
                League league = footballDataService.getLeagueData(leagueName);
                logger.info("Loaded and persisted data for league: {} Season: {}", league.getName(), league.getSeason());
            } catch (IOException e) {
                logger.error("Failed to load data for {}: {}", leagueName, e.getMessage());
            } catch (IllegalArgumentException e) {
                logger.error("Invalid league configuration for {}: {}", leagueName, e.getMessage());
            } catch (Exception e) {
                logger.error("An error occurred while processing {}: {}", leagueName, e.getMessage());
            }
        }

        // Load players after leagues and teams have been loaded
        try {
            playerService.loadPlayersFromCSV();
            logger.info("Players loaded successfully from CSV.");
        } catch (Exception e) {
            logger.error("Failed to load players from CSV: {}", e.getMessage());
            // Depending on requirements, decide whether to rethrow or handle differently
            throw e;
        }
    }
}
