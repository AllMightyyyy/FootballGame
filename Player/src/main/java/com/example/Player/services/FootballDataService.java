// src/main/java/com/example/Player/services/FootballDataService.java

package com.example.Player.services;

import com.example.Player.models.League;
import com.example.Player.repository.LeagueRepository;
import com.example.Player.utils.*;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

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

    @Autowired
    private LeagueConfig leagueConfig;

    @Autowired
    private StandingService standingService; // Inject StandingService

    private static final Logger logger = LoggerFactory.getLogger(FootballDataService.class);

    public FootballDataService() {
        // Initialize the mapping of full league names to JSON file names
        leagueFileMap = new HashMap<>();
        leagueFileMap.put("English Premier League 2024/25", "en.1.json");
        leagueFileMap.put("Italian Serie A 2024/25", "it.1.json");
        leagueFileMap.put("French Ligue 1 2024/25", "fr.1.json");
        leagueFileMap.put("Primera División de España 2024/25", "es.1.json");
        leagueFileMap.put("Deutsche Bundesliga 2024/25", "de.1.json");
        // Add other leagues as needed
    }

    /**
     * Check if a league exists based on its full name.
     */
    public boolean leagueExists(String leagueName) {
        Optional<League> leagueOpt = leagueRepository.findByName(leagueName);
        return leagueOpt.isPresent();
    }

    public League getLeagueDataByCode(String leagueCode) throws IOException {
        Optional<League> leagueOpt = leagueRepository.findByCode(leagueCode);
        if (leagueOpt.isPresent()) {
            return leagueOpt.get();
        } else {
            // Load league data from JSON or external source
            League league = jsonDataLoader.loadLeagueData(leagueCode + ".json"); // Assuming JSON files are named by league codes
            return league;
        }
    }

    public League getLeagueData(String leagueName) throws IOException {
        // Trim and validate the league name
        leagueName = leagueName.trim();

        logger.info("League Name: {}", leagueName);
        String fileName = leagueFileMap.get(leagueName);

        if (fileName == null) {
            throw new IllegalArgumentException("Unknown league: " + leagueName);
        }

        logger.info("File Name: {}", fileName);
        League league = jsonDataLoader.loadLeagueData(fileName);

        Hibernate.initialize(league.getTeams());
        Hibernate.initialize(league.getMatches());

        return league;
    }

    public Optional<League> getLeagueByCode(String leagueCode) {
        return leagueRepository.findByCode(leagueCode);
    }

    public Optional<League> getLeagueByCodeAndSeason(String leagueCode, String season) {
        return leagueRepository.findByCodeAndSeason(leagueCode, season);
    }

    public LeagueDTO getStandingsDTO(String leagueName) throws IOException {
        League league = getLeagueData(leagueName);
        LeagueDTO dto = LeagueMapper.INSTANCE.leagueToLeagueDTO(league);
        dto.setStandings(standingService.calculateStandings(league)); // Use StandingService
        return dto;
    }

    public LeagueDTO getStandingsDTOByCode(String leagueCode) {
        Optional<League> leagueOpt = leagueRepository.findByCode(leagueCode);
        if (leagueOpt.isEmpty()) {
            throw new IllegalArgumentException("League not found with code: " + leagueCode);
        }
        League league = leagueOpt.get();
        // Calculate standings based on matches
        List<StandingDTO> standings = standingService.calculateStandings(league); // Use StandingService
        // Map League to LeagueDTO
        LeagueDTO dto = LeagueMapper.INSTANCE.leagueToLeagueDTO(league);
        dto.setStandings(standings);
        return dto;
    }
}
