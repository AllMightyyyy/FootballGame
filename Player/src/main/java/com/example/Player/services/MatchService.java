// src/main/java/com/example/Player/services/MatchService.java

package com.example.Player.services;

import com.example.Player.models.League;
import com.example.Player.models.Match;
import com.example.Player.models.Team;
import com.example.Player.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamService teamService;

    public void persistMatches(List<Match> matches, League league) {
        for (Match match : matches) {
            // Fetch Team entities by name
            Team team1 = teamService.getTeamByName(match.getTeam1().getName(), league);
            Team team2 = teamService.getTeamByName(match.getTeam2().getName(), league);

            if (team1 != null && team2 != null) {
                match.setTeam1(team1);
                match.setTeam2(team2);
                match.setLeague(league);
                matchRepository.save(match);
            } else {
                System.err.println("One of the teams not found for match: "
                        + match.getTeam1().getName() + " vs " + match.getTeam2().getName());
            }
        }
    }
}
