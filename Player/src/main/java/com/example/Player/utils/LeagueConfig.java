// src/main/java/com/example/Player/utils/LeagueConfig.java

package com.example.Player.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LeagueConfig {

    private Map<String, LeagueDetails> leagues = new HashMap<>();

    public LeagueConfig() {
        // Initialize league configurations
        leagues.put("en.1", new LeagueDetails("English Premier League", "en.1", "2024-25"));
        leagues.put("it.1", new LeagueDetails("Italian Serie A", "it.1", "2024-25"));
        leagues.put("fr.1", new LeagueDetails("French Ligue 1", "fr.1", "2024-25"));
        leagues.put("es.1", new LeagueDetails("Primera División de España", "es.1", "2024-25"));
        leagues.put("de.1", new LeagueDetails("German 1. Bundesliga", "de.1", "2024-25"));
        // Add other leagues as needed
    }

    public Map<String, LeagueDetails> getLeagues() {
        return leagues;
    }

    public static class LeagueDetails {
        private String name;
        private String code;
        private String season;

        public LeagueDetails(String name, String code, String season) {
            this.name = name;
            this.code = code;
            this.season = season;
        }

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getSeason() {
            return season;
        }

        public void setSeason(String season) {
            this.season = season;
        }
    }
}
