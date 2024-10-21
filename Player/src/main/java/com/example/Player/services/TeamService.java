package com.example.Player.services;

import com.example.Player.models.League;
import com.example.Player.models.Match;
import com.example.Player.models.Team;
import com.example.Player.models.User;
import com.example.Player.repository.MatchRepository;
import com.example.Player.repository.TeamRepository;
import com.example.Player.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private MatchRepository matchRepository;

    private TeamService teamService;

    // Get all teams by league
    public List<Team> getTeamsByLeague(League league) {
        return teamRepository.findAllByLeague(league);
    }

    // Assign team to user
    public boolean assignTeamToUser(String leagueCode, String teamName, User user) {
        Optional<League> leagueOpt = leagueService.getCurrentLeagueByCode(leagueCode);
        if (leagueOpt.isEmpty()) {
            return false;
        }
        League league = leagueOpt.get();
        Optional<Team> teamOpt = teamRepository.findByNameAndLeague(teamName, league);
        if (teamOpt.isEmpty()) {
            return false;
        }
        Team team = teamOpt.get();

        if (team.getUser() != null) {
            return false; // Team already occupied
        }

        // Assign team to user
        team.setUser(user);
        teamRepository.save(team);

        // Assign team to user entity
        user.setTeam(team);
        userRepository.save(user);

        return true;
    }

    // Check if a team is available for assignment
    public boolean isTeamAvailable(String leagueCode, String teamName) {
        Optional<League> leagueOpt = leagueService.getCurrentLeagueByCode(leagueCode);
        if (leagueOpt.isEmpty()) {
            return false;
        }
        League league = leagueOpt.get();
        Optional<Team> teamOpt = teamRepository.findByNameAndLeague(teamName, league);
        return teamOpt.isPresent() && teamOpt.get().getUser() == null;
    }

    // Get user's team
    public Optional<Team> getUserTeam(User user) {
        return teamRepository.findByUser(user);
    }

    // Create a new team if it doesn't exist
    public Team createTeamIfNotExists(String teamName, League league) {
        Optional<Team> teamOpt = teamRepository.findByNameAndLeague(teamName, league);
        if (teamOpt.isPresent()) {
            return teamOpt.get();
        } else {
            Team team = new Team();
            team.setName(teamName);
            team.setLeague(league);
            return teamRepository.save(team);
        }
    }

    // Get Team by name and league
    public Team getTeamByName(String teamName, League league) {
        return teamRepository.findByNameAndLeague(teamName, league).orElse(null);
    }

    public Optional<Team> getTeamByName(String teamName) {
        return teamRepository.findByName(teamName);
    }

    // Fetch team by name and league code
    public Team getTeamByNameAndLeagueCode(String teamName, String leagueCode) {
        Optional<League> leagueOpt = leagueService.getCurrentLeagueByCode(leagueCode);
        if (leagueOpt.isEmpty()) {
            return null;
        }
        return teamRepository.findByNameAndLeague(teamName, leagueOpt.get()).orElse(null);
    }

    // Additional Methods
    public boolean existsByName(String name) {
        return teamRepository.existsByName(name);
    }

    public void saveTeam(Team team) {
        teamRepository.save(team);
    }
}