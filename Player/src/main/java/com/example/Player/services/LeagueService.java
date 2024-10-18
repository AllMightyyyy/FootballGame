// src/main/java/com/example/Player/services/LeagueService.java

package com.example.Player.services;

import com.example.Player.models.League;
import com.example.Player.repository.LeagueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LeagueService {

    @Autowired
    private LeagueRepository leagueRepository;

    public Optional<League> getLeagueByName(String name) {
        return leagueRepository.findByName(name);
    }

    public League saveLeague(League league) {
        return leagueRepository.save(league);
    }
}
