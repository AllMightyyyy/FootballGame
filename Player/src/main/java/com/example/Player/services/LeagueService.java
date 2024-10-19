package com.example.Player.services;

import com.example.Player.models.League;
import com.example.Player.repository.LeagueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeagueService {

    @Autowired
    private LeagueRepository leagueRepository;

    // Fetch league by name
    public Optional<League> getLeagueByName(String name) {
        return leagueRepository.findByName(name);
    }

    // Save league
    public League saveLeague(League league) {
        return leagueRepository.save(league);
    }

    // Get all league names
    public List<String> getAllLeagueNames() {
        return leagueRepository.findAll().stream()
                .map(League::getName)
                .collect(Collectors.toList());
    }

    // Fetch league by code
    public Optional<League> getLeagueByCode(String code) {
        return leagueRepository.findByCode(code);
    }

    // Fetch league by code and season
    public Optional<League> getLeagueByCodeAndSeason(String code, String season) {
        return leagueRepository.findByCodeAndSeason(code, season);
    }

    // Get all league codes
    public List<String> getAllLeagueCodes() {
        return leagueRepository.findAll().stream()
                .map(League::getCode)
                .collect(Collectors.toList());
    }

    // Fetch all leagues
    public List<League> getAllLeagues() {
        return leagueRepository.findAll();
    }

    public Optional<League> getCurrentLeagueByCode(String code) {
        return leagueRepository.findTopByCodeOrderBySeasonDesc(code);
    }

    public Optional<League> findLeagueByNameStartingWith(String name) {
        List<League> leagues = leagueRepository.findByNameStartingWith(name);
        if (!leagues.isEmpty()) {
            // Assuming the latest season is needed
            return leagues.stream()
                    .max(Comparator.comparing(League::getSeason));
        }
        return Optional.empty();
    }
}
