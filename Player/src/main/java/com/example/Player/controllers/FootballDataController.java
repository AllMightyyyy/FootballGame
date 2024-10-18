// src/main/java/com/example/Player/controllers/FootballDataController.java

package com.example.Player.controllers;

import com.example.Player.models.League;
import com.example.Player.models.Match;
import com.example.Player.services.FootballDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            return footballDataService.getLeagueData(leagueName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load league data", e);
        }
    }

    @GetMapping("/standings/{leagueName}")
    public List<Match> getStandings(@PathVariable String leagueName) {
        try {
            League league = footballDataService.getLeagueData(leagueName);
            return league.getMatches();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load league data", e);
        }
    }
}
