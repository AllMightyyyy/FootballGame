// src/main/java/com/example/Player/utils/TeamDataLoader.java
package com.example.Player.utils;

import com.example.Player.services.TeamService;
import com.example.Player.services.FootballDataService;
import com.example.Player.models.League;
import com.example.Player.models.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class TeamDataLoader implements CommandLineRunner {

    @Autowired
    private FootballDataService footballDataService;

    @Autowired
    private TeamService teamService;

    @Override
    public void run(String... args) throws Exception {
        // Define the 6 leagues
        List<String> leagues = List.of("en.1", "es.1", "de.1", "it.1", "fr.1", "uefa.cl");

        // For each league, get team names from matches and create teams
        for (String leagueAbbr : leagues) {
            try {
                League league = footballDataService.getLeagueData(leagueAbbr);
                Set<String> teamNames = new HashSet<>();
                for (Match match : league.getMatches()) {
                    teamNames.add(match.getTeam1());
                    teamNames.add(match.getTeam2());
                }

                // Create teams in the database
                for (String teamName : teamNames) {
                    teamService.createTeam(teamName, league.getName());
                }

                System.out.println("Teams for league " + league.getName() + " loaded.");

            } catch (IOException e) {
                System.out.println("Failed to load league data for " + leagueAbbr + ": " + e.getMessage());
            }
        }
    }
}
