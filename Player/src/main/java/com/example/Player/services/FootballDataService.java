// src/main/java/com/example/Player/services/FootballDataService.java

package com.example.Player.services;

import com.example.Player.models.League;
import com.example.Player.repository.LeagueRepository;
import com.example.Player.utils.JsonDataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class FootballDataService {

    private final Map<String, String> leagueFileMap;

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private JsonDataLoader jsonDataLoader;

    public FootballDataService() {
        // Initialize the mapping of full league names to JSON file names
        leagueFileMap = new HashMap<>();
        leagueFileMap.put("English Premier League", "EnglishPremierLeague.json");
        leagueFileMap.put("Spain Primera Division", "SpainPrimeraDivision.json");
        leagueFileMap.put("German 1. Bundesliga", "German1Bundesliga.json");
        leagueFileMap.put("Italian Serie A", "ItalianSerieA.json");
        leagueFileMap.put("French Ligue 1", "FrenchLigue1.json");
        // Add other leagues as needed
    }

    public League getLeagueData(String leagueName) throws IOException {
        // Trim and validate the league name
        leagueName = leagueName.trim();

        System.out.println("League Name: " + leagueName);
        String fileName = leagueFileMap.get(leagueName);

        if (fileName == null) {
            throw new IllegalArgumentException("Unknown league: " + leagueName);
        }

        System.out.println("File Name: " + fileName);
        League league = jsonDataLoader.loadLeagueData(fileName);

        // Check if league already exists in the database
        Optional<League> existingLeagueOpt = leagueService.getLeagueByName(league.getName());
        League persistedLeague;
        if (existingLeagueOpt.isPresent()) {
            persistedLeague = existingLeagueOpt.get();
        } else {
            persistedLeague = leagueService.saveLeague(new League(league.getName()));
        }

        // Persist Teams and Matches
        if (league.getMatches() != null) {
            teamService.persistTeams(league.getMatches(), persistedLeague);
            matchService.persistMatches(league.getMatches(), persistedLeague);
        }

        return persistedLeague;
    }
}
