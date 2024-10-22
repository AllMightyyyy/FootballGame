package com.example.Player.services;

import com.example.Player.DTO.*;
import com.example.Player.models.*;
import com.example.Player.repository.FantasyLeagueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FantasyLeagueService {

    @Autowired
    private FantasyLeagueRepository fantasyLeagueRepository;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private FantasyPlayerService fantasyPlayerService;

    public FantasyLeagueDTO createFantasyLeague(String realLeagueCode, String fantasyLeagueName) throws Exception {
        // Business logic to create a FantasyLeague
        // 1. Fetch the real league by code
        League realLeague = leagueService.getLeagueByCode(realLeagueCode)
                .orElseThrow(() -> new Exception("Real League not found with code: " + realLeagueCode));

        // 2. Check if a FantasyLeague already exists for this real league
        if (fantasyLeagueRepository.findByRealLeague(realLeague).isPresent()) {
            throw new Exception("Fantasy League already exists for this Real League.");
        }

        // 3. Create and save the FantasyLeague entity
        FantasyLeague fantasyLeague = new FantasyLeague();
        fantasyLeague.setName(fantasyLeagueName);
        fantasyLeague.setRealLeague(realLeague);
        // Initialize other fields as necessary

        fantasyLeague = fantasyLeagueRepository.save(fantasyLeague);

        // 4. Map FantasyLeague entity to FantasyLeagueDTO
        FantasyLeagueDTO fantasyLeagueDTO = mapToDTO(fantasyLeague);

        return fantasyLeagueDTO;
    }

    private FantasyLeagueDTO mapToDTO(FantasyLeague fantasyLeague) {
        FantasyLeagueDTO dto = new FantasyLeagueDTO();
        dto.setId(fantasyLeague.getId());
        dto.setName(fantasyLeague.getName());

        // Map realLeague
        LeagueDTO leagueDTO = mapLeagueToDTO(fantasyLeague.getRealLeague());
        dto.setRealLeague(leagueDTO);

        // Map fantasyTeams
        List<TeamDTO> teamDTOs = fantasyLeague.getFantasyTeams().stream()
                .map(this::mapTeamToDTO)
                .collect(Collectors.toList());
        dto.setFantasyTeams(teamDTOs);

        // Map availablePlayers if applicable
        // List<FantasyPlayerDTO> playerDTOs = fantasyLeague.getAvailablePlayers().stream()
        //         .map(this::mapFantasyPlayerToDTO)
        //         .collect(Collectors.toList());
        // dto.setAvailablePlayers(playerDTOs);

        return dto;
    }

    private LeagueDTO mapLeagueToDTO(League league) {
        LeagueDTO dto = new LeagueDTO();
        dto.setName(league.getName());
        dto.setCode(league.getCode());
        dto.setSeason(league.getSeason());

        // Map matches without scores
        List<MatchDTO> matchDTOs = league.getMatches().stream()
                .map(this::mapMatchToDTOExcludingScore)
                .collect(Collectors.toList());
        dto.setMatches(matchDTOs);

        // Map standings if applicable
        List<StandingDTO> standingDTOs = league.getStandings().stream()
                .map(this::mapStandingToDTO)
                .collect(Collectors.toList());
        dto.setStandings(standingDTOs);

        return dto;
    }

    private MatchDTO mapMatchToDTOExcludingScore(Match match) {
        MatchDTO dto = new MatchDTO();
        dto.setRound(match.getRound());
        dto.setDate(match.getDate().toString());
        dto.setTime(match.getTime().toString());
        dto.setTeam1(match.getTeam1().getName());
        dto.setTeam2(match.getTeam2().getName());
        dto.setStatus(match.getStatus()); // Assuming Match has a status field
        // Exclude score by not setting it
        return dto;
    }

    private TeamDTO mapTeamToDTO(Team team) {
        TeamDTO dto = new TeamDTO();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setLeagueCode(team.getLeague().getCode());
        return dto;
    }

    private StandingDTO mapStandingToDTO(Standing standing) {
        StandingDTO dto = new StandingDTO();
        dto.setTeamName(standing.getTeamName());
        dto.setPlayed(standing.getPlayed());
        dto.setWin(standing.getWin());
        dto.setDraw(standing.getDraw());
        dto.setLoss(standing.getLoss());
        dto.setGoalsFor(standing.getGoalsFor());
        dto.setGoalsAgainst(standing.getGoalsAgainst());
        dto.setGoalDifference(standing.getGoalDifference());
        dto.setPoints(standing.getPoints());
        return dto;
    }

    // If you have FantasyPlayerDTO
    /*
    private FantasyPlayerDTO mapFantasyPlayerToDTO(FantasyPlayer fantasyPlayer) {
        FantasyPlayerDTO dto = new FantasyPlayerDTO();
        dto.setId(fantasyPlayer.getId());
        dto.setName(fantasyPlayer.getName());
        dto.setPosition(fantasyPlayer.getPosition());
        // Map other fields as necessary
        return dto;
    }
    */

    private void clonePlayers(League realLeague, FantasyLeague fantasyLeague) {
        List<Player> realPlayers = playerService.getPlayersByLeague(realLeague);
        for (Player realPlayer : realPlayers) {
            FantasyPlayer fantasyPlayer = new FantasyPlayer();
            fantasyPlayer.setRealPlayer(realPlayer);
            fantasyPlayer.setFantasyLeague(fantasyLeague);
            fantasyPlayer.setPrice(calculatePlayerPrice(realPlayer));
            fantasyPlayer.setStamina(100.0);
            fantasyPlayerService.saveFantasyPlayer(fantasyPlayer);
        }
    }

    private double calculatePlayerPrice(Player player) {
        // Example pricing logic based on player stats
        return (player.getOverall() + player.getPotential()) * 1000;
    }

    public Optional<FantasyLeague> getFantasyLeagueByRealLeague(League realLeague) {
        return fantasyLeagueRepository.findByRealLeague(realLeague);
    }

    /**
     * Added method to get all fantasy leagues.
     */
    public List<FantasyLeague> getAllLeagues() {
        return fantasyLeagueRepository.findAll();
    }

    // Additional methods as needed
}
