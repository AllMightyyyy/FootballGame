package com.example.Player.services;

import com.example.Player.models.League;
import com.example.Player.utils.JsonDataLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FootballDataService {
    private final Map<String, String> leagueFileMap;

    public FootballDataService() {
        // Initialize the mapping of league abbreviations to file names
        leagueFileMap = new HashMap<>();
        leagueFileMap.put("en.1", "en.1.json");
        leagueFileMap.put("es.1", "es.1.json");
        leagueFileMap.put("de.1", "de.1.json");
        leagueFileMap.put("it.1", "it.1.json");
        leagueFileMap.put("fr.1", "fr.1.json");
        leagueFileMap.put("uefa.cl", "uefa.cl.json");
    }

    public League getLeagueData(String leagueAbbreviation) throws IOException {
        // Remove '.json' extension if present
        if (leagueAbbreviation.endsWith(".json")) {
            leagueAbbreviation = leagueAbbreviation.substring(0, leagueAbbreviation.length() - 5);
        }

        System.out.println("League Abbreviation: " + leagueAbbreviation);
        String fileName = leagueFileMap.get(leagueAbbreviation);

        if (fileName == null) {
            throw new IllegalArgumentException("Unknown league: " + leagueAbbreviation);
        }

        System.out.println("File Name: " + fileName);
        return JsonDataLoader.loadLeagueData(fileName);
    }


}
