package com.example.Player.controllers;

import com.example.Player.models.League;
import com.example.Player.models.Match;
import com.example.Player.services.FootballDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/football")
public class FootballDataController {
    private final FootballDataService footballDataService;

    @Autowired
    public FootballDataController(FootballDataService footballDataService) {
        this.footballDataService = footballDataService;
    }

    @GetMapping("/league/{leagueName}")
    public League getLeagueData(@PathVariable String leagueName) {
        try {
            // Pass the leagueName directly
            return footballDataService.getLeagueData(leagueName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load league data", e);
        }
    }

    @GetMapping("/standings/{leagueAbbreviation}")
    public List<Match> getStandings(@PathVariable String leagueAbbreviation) {
        try {
            // Corrected: Do not append ".json" here
            League league = footballDataService.getLeagueData(leagueAbbreviation);

            // Return the matches to calculate standings on the frontend
            return league.getMatches();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load league data", e);
        }
    }
}
