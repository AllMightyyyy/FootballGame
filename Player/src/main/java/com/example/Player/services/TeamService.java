// src/main/java/com/example/Player/services/TeamService.java
package com.example.Player.services;

import com.example.Player.models.Team;
import com.example.Player.models.User;
import com.example.Player.repository.TeamRepository;
import com.example.Player.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    // Get all teams in a specific league
    public List<Team> getTeamsByLeague(String league) {
        return teamRepository.findAll().stream()
                .filter(team -> team.getLeague().equalsIgnoreCase(league))
                .toList();
    }

    // Assign a team to a user
    @Transactional
    public boolean assignTeamToUser(String league, String teamName, User user) {
        Optional<Team> teamOpt = teamRepository.findByNameAndLeague(teamName, league);
        if (teamOpt.isPresent()) {
            Team team = teamOpt.get();
            if (team.getUser() == null) {
                team.setUser(user);
                user.setTeam(team);
                userRepository.save(user);
                teamRepository.save(team);
                return true;
            }
        }
        return false;
    }

    // Check if a team is available for assignment
    public boolean isTeamAvailable(String league, String teamName) {
        Optional<Team> teamOpt = teamRepository.findByNameAndLeague(teamName, league);
        return teamOpt.isPresent() && teamOpt.get().getUser() == null;
    }

    // Retrieve the team managed by a user
    public Optional<Team> getUserTeam(User user) {
        return Optional.ofNullable(user.getTeam());
    }

    // Create a new team
    public void createTeam(String name, String league) {
        if (!teamRepository.existsByNameAndLeague(name, league)) {
            Team team = new Team();
            team.setName(name);
            team.setLeague(league);
            teamRepository.save(team);
        }
    }
}
