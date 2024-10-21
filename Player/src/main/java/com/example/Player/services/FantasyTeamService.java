package com.example.Player.services;

import com.example.Player.models.FantasyLeague;
import com.example.Player.models.FantasyPlayer;
import com.example.Player.models.FantasyTeam;
import com.example.Player.models.User;
import com.example.Player.repository.FantasyLeagueRepository;
import com.example.Player.repository.FantasyTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FantasyTeamService {

    @Autowired
    private FantasyTeamRepository fantasyTeamRepository;

    @Autowired
    private FantasyLeagueRepository fantasyLeagueRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FantasyPlayerService fantasyPlayerService;

    public FantasyTeam chooseFantasyTeam(User user, String teamName, FantasyLeague fantasyLeague) throws Exception {
        if (fantasyTeamRepository.findByOwner(user).isPresent()) {
            throw new Exception("User already has a Fantasy Team.");
        }

        if (fantasyTeamRepository.existsByFantasyLeagueAndTeamName(fantasyLeague, teamName)) {
            throw new Exception("Team name already taken in this Fantasy League.");
        }

        FantasyTeam fantasyTeam = new FantasyTeam();
        fantasyTeam.setTeamName(teamName);
        fantasyTeam.setFantasyLeague(fantasyLeague);
        fantasyTeam.setOwner(user);
        fantasyTeam.setBalance(determineInitialBalance(teamName, fantasyLeague));
        fantasyTeamRepository.save(fantasyTeam);

        return fantasyTeam;
    }

    private double determineInitialBalance(String teamName, FantasyLeague fantasyLeague) {
        // Determine balance based on team stature, sponsors, stadium capacity, etc.
        // For simplicity, assign a flat rate. Enhance as per actual logic.
        return 1_000_000.0;
    }

    public Optional<FantasyTeam> getFantasyTeamByUser(User user) {
        return fantasyTeamRepository.findByOwner(user);
    }

    public void assignPlayerToTeam(FantasyTeam fantasyTeam, FantasyPlayer fantasyPlayer) throws Exception {
        if (!fantasyPlayer.isInjured() && fantasyPlayer.getStamina() > 0 && fantasyPlayer.getFantasyLeague().equals(fantasyTeam.getFantasyLeague())) {
            fantasyTeam.getPlayers().add(fantasyPlayer);
            fantasyPlayer.setFantasyTeam(fantasyTeam);
            fantasyPlayerService.saveFantasyPlayer(fantasyPlayer);
            fantasyTeamRepository.save(fantasyTeam);
        } else {
            throw new Exception("Player is unavailable for assignment.");
        }
    }

    // Additional methods for managing lineup, substitutions, etc.
}
