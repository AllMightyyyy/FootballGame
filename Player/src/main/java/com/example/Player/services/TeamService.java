package com.example.Player.services;

import com.example.Player.models.League;
import com.example.Player.models.Match;
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

    @Autowired
    private LeagueService leagueService;

    // Get all teams in a league
    public List<Team> getTeamsByLeague(String leagueName) {
        Optional<League> leagueOpt = leagueService.getLeagueByName(leagueName);
        return leagueOpt.map(league -> teamRepository.findAllByLeague(league)).orElse(List.of());
    }

    // Assign a team to a user
    @Transactional
    public boolean assignTeamToUser(String leagueName, String teamName, User user) {
        Optional<League> leagueOpt = leagueService.getLeagueByName(leagueName);
        if (leagueOpt.isEmpty()) {
            return false; // League not found
        }
        League league = leagueOpt.get();

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
        return false; // Team is already occupied or does not exist
    }

    // Check if a team is available for assignment
    public boolean isTeamAvailable(String leagueName, String teamName) {
        Optional<League> leagueOpt = leagueService.getLeagueByName(leagueName);
        if (leagueOpt.isEmpty()) {
            return false;
        }
        League league = leagueOpt.get();
        Optional<Team> teamOpt = teamRepository.findByNameAndLeague(teamName, league);
        return teamOpt.isPresent() && teamOpt.get().getUser() == null;
    }

    // Retrieve the team managed by a user
    public Optional<Team> getUserTeam(User user) {
        return Optional.ofNullable(user.getTeam());
    }

    // Create a new team if it doesn't exist
    public Team createTeamIfNotExists(String name, League league) {
        return teamRepository.findByNameAndLeague(name, league)
                .orElseGet(() -> {
                    Team newTeam = new Team();
                    newTeam.setName(name);
                    newTeam.setLeague(league);
                    return teamRepository.save(newTeam);
                });
    }


    // Persist teams from matches
    public void persistTeams(List<Match> matches, League league) {
        for (Match match : matches) {
            String team1Name = match.getTeam1().getName();
            String team2Name = match.getTeam2().getName();

            createTeamIfNotExists(team1Name, league);
            createTeamIfNotExists(team2Name, league);
        }
    }

    // Get Team by name and league
    public Team getTeamByName(String name, League league) {
        return teamRepository.findByNameAndLeague(name, league).orElse(null);
    }


    // Additional Methods
    public boolean existsByName(String name) {
        return teamRepository.existsByName(name);
    }

    public void saveTeam(Team team) {
        teamRepository.save(team);
    }
}
