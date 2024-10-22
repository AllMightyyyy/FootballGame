package com.example.Player.services;

import com.example.Player.DTO.FantasyLeagueDTO;
import com.example.Player.DTO.FantasyPlayerDTO;
import com.example.Player.DTO.LeagueDTO;
import com.example.Player.DTO.MatchDTO;
import com.example.Player.DTO.TeamDTO;
import com.example.Player.mapper.LeagueMapper;
import com.example.Player.models.*;
import com.example.Player.repository.FantasyLeagueRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class FantasyLeagueService {

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private FantasyLeagueRepository fantasyLeagueRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private LeagueMapper leagueMapper;

    /**
     * Create a Fantasy League based on a Real League.
     */
    @Transactional
    public FantasyLeagueDTO createFantasyLeague(String realLeagueCode, String fantasyLeagueName) throws Exception {
        // Fetch the real league by code
        League realLeague = leagueService.getLeagueByCode(realLeagueCode)
                .orElseThrow(() -> new Exception("Real League not found with code: " + realLeagueCode));

        // Check if Fantasy League already exists
        if (fantasyLeagueRepository.findByRealLeague(realLeague).isPresent()) {
            throw new Exception("Fantasy League already exists for this Real League.");
        }

        // Create the Fantasy League
        FantasyLeague fantasyLeague = new FantasyLeague();
        fantasyLeague.setName(fantasyLeagueName);
        fantasyLeague.setRealLeague(realLeague);

        // Copy and adjust matches
        FantasyLeague finalFantasyLeague = fantasyLeague;
        List<Match> fantasyMatches = realLeague.getMatches().stream()
                .map(realMatch -> createFantasyMatchFromRealMatch(realMatch, finalFantasyLeague))
                .collect(Collectors.toList());

        fantasyLeague.setMatches(fantasyMatches);
        fantasyLeague = fantasyLeagueRepository.save(fantasyLeague);

        // Map to DTO and return using the custom method
        return mapToDTO(fantasyLeague);
    }

    // New method to get FantasyLeague by real league code
    public Optional<FantasyLeague> getFantasyLeagueByRealLeagueCode(String realLeagueCode) {
        return fantasyLeagueRepository.findByRealLeague_Code(realLeagueCode);
    }

    public Optional<FantasyLeague> getFantasyLeagueByRealLeague(League realLeague) {
        // Use league code as it is unique to identify the FantasyLeague
        return fantasyLeagueRepository.findByRealLeague_Code(realLeague.getCode());
    }

    /**
     * Map FantasyLeague to FantasyLeagueDTO.
     */
    private FantasyLeagueDTO mapToDTO(FantasyLeague fantasyLeague) {
        FantasyLeagueDTO dto = new FantasyLeagueDTO();
        dto.setId(fantasyLeague.getId());
        dto.setName(fantasyLeague.getName());

        // Map realLeague
        LeagueDTO leagueDTO = mapLeagueToDTO(fantasyLeague.getRealLeague());
        dto.setRealLeague(leagueDTO);

        // Map fantasyTeams
        List<TeamDTO> teamDTOs = fantasyLeague.getFantasyTeams().stream()
                .map(this::mapFantasyTeamToDTO)
                .collect(Collectors.toList());
        dto.setFantasyTeams(teamDTOs);

        // Map availablePlayers
        List<FantasyPlayerDTO> playerDTOs = fantasyLeague.getAvailablePlayers().stream()
                .map(this::mapFantasyPlayerToDTO)
                .collect(Collectors.toList());
        dto.setAvailablePlayers(playerDTOs);

        return dto;
    }

    /**
     * Map League to LeagueDTO.
     */
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

        return dto;
    }

    /**
     * Map Match to MatchDTO excluding score.
     */
    private MatchDTO mapMatchToDTOExcludingScore(Match match) {
        MatchDTO dto = new MatchDTO();
        dto.setRound(match.getRound());
        dto.setDate(match.getDate().toString());
        dto.setTime(match.getTime().toString());
        dto.setTeam1(match.getTeam1().getName());
        dto.setTeam2(match.getTeam2().getName());
        dto.setStatus(match.getStatus());
        // Exclude score by not setting it
        return dto;
    }

    private TeamDTO mapFantasyTeamToDTO(FantasyTeam fantasyTeam) {
        TeamDTO dto = new TeamDTO();
        // Assuming FantasyTeam has a getTeam() method to access the Team entity
        Team team = fantasyTeam.getTeam(); // Adjust this line based on your actual entity structure
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setLeagueCode(team.getLeague().getCode());
        return dto;
    }

    private FantasyPlayerDTO mapFantasyPlayerToDTO(FantasyPlayer fantasyPlayer) {
        FantasyPlayerDTO dto = new FantasyPlayerDTO();
        dto.setId(fantasyPlayer.getId());

        // Access name and positions through the associated realPlayer
        if (fantasyPlayer.getRealPlayer() != null) {
            dto.setName(fantasyPlayer.getRealPlayer().getLongName());
            // Join the list of positions into a comma-separated string
            dto.setPosition(String.join(", ", fantasyPlayer.getRealPlayer().getPositionsList()));
        } else {
            dto.setName("Unknown Player");
            dto.setPosition("Unknown Position");
        }

        // Map other necessary fields from FantasyPlayer if required
        // For example:
        // dto.setPrice(fantasyPlayer.getPrice());
        // dto.setStamina(fantasyPlayer.getStamina());
        // dto.setInjured(fantasyPlayer.isInjured());
        // dto.setAssigned(fantasyPlayer.isAssigned());
        // dto.setPenaltyTaker(fantasyPlayer.isPenaltyTaker());
        // dto.setCornerTaker(fantasyPlayer.isCornerTaker());
        // dto.setFreeKickTaker(fantasyPlayer.isFreeKickTaker());

        return dto;
    }

    /**
     * Create a Fantasy Match based on a Real Match.
     */
    private Match createFantasyMatchFromRealMatch(Match realMatch, FantasyLeague fantasyLeague) {
        Match fantasyMatch = new Match();
        fantasyMatch.setRound(realMatch.getRound());
        fantasyMatch.setDate(realMatch.getDate());
        fantasyMatch.setTeam1(realMatch.getTeam1());
        fantasyMatch.setTeam2(realMatch.getTeam2());

        // Assign default or random time if the real match has no time
        if (realMatch.getTime() == null || realMatch.getTime().isEmpty()) {
            fantasyMatch.setTime(generateRandomTime()); // Generate random time
        } else {
            fantasyMatch.setTime(realMatch.getTime());
        }

        // Copy score and other fields as necessary
        fantasyMatch.setScore(realMatch.getScore());
        fantasyMatch.setStatus(realMatch.getStatus());

        // Associate with Fantasy League
        fantasyMatch.setFantasyLeague(fantasyLeague);

        return fantasyMatch;
    }

    /**
     * Generate a random time in the format "HH:mm".
     */
    private String generateRandomTime() {
        Random rand = new Random();
        int hour = rand.nextInt(10) + 10; // Generates hour between 10 and 19
        int minute = rand.nextBoolean() ? 0 : 30; // Either 00 or 30 minutes
        return String.format("%02d:%02d", hour, minute); // e.g., "14:30"
    }
}
