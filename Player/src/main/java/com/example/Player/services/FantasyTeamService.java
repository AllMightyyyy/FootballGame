// FantasyTeamService.java
package com.example.Player.services;

import com.example.Player.DTO.FantasyLeagueDTO;
import com.example.Player.DTO.FantasyTeamDTO;
import com.example.Player.DTO.TeamDTO;
import com.example.Player.DTO.UserDTO;
import com.example.Player.models.*;
import com.example.Player.repository.FantasyLeagueRepository;
import com.example.Player.repository.FantasyTeamRepository;
import com.example.Player.repository.StadiumRepository;
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

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private ObjectiveService objectiveService;

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private TeamService teamService;

    public FantasyTeamDTO chooseFantasyTeam(User user, String teamName, FantasyLeague fantasyLeague) throws Exception {
        // Check if user already has a fantasy team
        if (fantasyTeamRepository.findByOwner(user).isPresent()) {
            throw new Exception("User already has a Fantasy Team.");
        }

        // Check if the team name is already taken in this Fantasy League
        if (fantasyTeamRepository.existsByFantasyLeagueAndTeamName(fantasyLeague, teamName)) {
            throw new Exception("Team name already taken in this Fantasy League.");
        }

        // Fetch the actual team by its name (assuming teamService provides this functionality)
        Team team = teamService.getTeamByName(teamName)
                .orElseThrow(() -> new Exception("Team not found: " + teamName));

        // Create the FantasyTeam and related entities
        FantasyTeam fantasyTeam = new FantasyTeam();
        fantasyTeam.setTeamName(teamName);
        fantasyTeam.setTeam(team);  // Set the team here
        fantasyTeam.setFantasyLeague(fantasyLeague);
        fantasyTeam.setOwner(user);
        fantasyTeam.setBalance(determineInitialBalance(teamName, fantasyLeague));

        // Initialize stadium, sponsors, objectives, lineup, etc.
        initializeFantasyTeamComponents(fantasyTeam);

        // Save the FantasyTeam, cascading will save related entities
        fantasyTeamRepository.save(fantasyTeam);

        return mapToDTO(fantasyTeam);
    }

    /**
     * Determines the initial balance based on team stature, sponsors, stadium capacity, etc.
     * Placeholder logic: Assign a flat rate. Enhance as per actual logic.
     */
    private double determineInitialBalance(String teamName, FantasyLeague fantasyLeague) {
        return 1_000_000.0;
    }

    /**
     * Initializes various components of the fantasy team.
     */
    private void initializeFantasyTeamComponents(FantasyTeam fantasyTeam) throws Exception {
        // Initialize stadium
        Stadium stadium = new Stadium();
        stadium.setStadiumName(fantasyTeam.getTeamName() + " Stadium");
        stadium.setCapacity(50000); // Default capacity
        stadium.setPitchQuality(1.0);
        stadium.setTrainingFacilities(1.0);
        stadium.setFantasyTeam(fantasyTeam); // Set the team for the stadium
        fantasyTeam.setStadium(stadium); // Set the stadium in the team

        // Initialize objectives
        Objective objective = new Objective();
        objective.setDescription("Win the League");
        objective.setFantasyTeam(fantasyTeam); // Link the objective to the team
        fantasyTeam.getObjectives().add(objective);

        // Initialize lineup
        Lineup lineup = new Lineup();
        lineup.setFormation("4-3-3");
        fantasyTeam.setLineup(lineup);
    }

    /**
     * Converts a FantasyTeam entity to FantasyTeamDTO.
     */
    public FantasyTeamDTO mapToDTO(FantasyTeam fantasyTeam) {
        TeamDTO teamDTO = new TeamDTO(fantasyTeam.getTeam().getId(), fantasyTeam.getTeam().getName(), fantasyTeam.getTeam().getLeague().getCode());
        UserDTO ownerDTO = new UserDTO(fantasyTeam.getOwner().getId(), fantasyTeam.getOwner().getUsername(), fantasyTeam.getOwner().getEmail(), null);
        FantasyLeagueDTO leagueDTO = new FantasyLeagueDTO(fantasyTeam.getFantasyLeague().getId(), fantasyTeam.getFantasyLeague().getName(), null, null);

        return new FantasyTeamDTO(fantasyTeam.getId(), fantasyTeam.getTeamName(), fantasyTeam.getBalance(), teamDTO, ownerDTO, leagueDTO);
    }

    /**
     * Retrieves the fantasy team associated with a user.
     */
    public Optional<FantasyTeam> getFantasyTeamByUser(User user) {
        return fantasyTeamRepository.findByOwner(user);
    }

    /**
     * Assigns a player to the fantasy team.
     */
    public void assignPlayerToTeam(FantasyTeam fantasyTeam, FantasyPlayer fantasyPlayer) throws Exception {
        if (!fantasyPlayer.isInjured() &&
                fantasyPlayer.getStamina() > 0 &&
                fantasyPlayer.getFantasyLeague().equals(fantasyTeam.getFantasyLeague())) {

            fantasyTeam.getPlayers().add(fantasyPlayer);
            fantasyPlayer.setFantasyTeam(fantasyTeam);
            fantasyPlayerService.saveFantasyPlayer(fantasyPlayer);
            fantasyTeamRepository.save(fantasyTeam);
        } else {
            throw new Exception("Player is unavailable for assignment.");
        }
    }

    /**
     * Retrieves the fantasy team by its ID.
     */
    public Optional<FantasyTeam> getFantasyTeamById(Long id) {
        return fantasyTeamRepository.findById(id);
    }

    /**
     * Saves the fantasy team.
     */
    public FantasyTeam saveFantasyTeam(FantasyTeam fantasyTeam) {
        return fantasyTeamRepository.save(fantasyTeam);
    }

    /**
     * Assigns a specialist role to a player within the team.
     */
    public void assignSpecialistRole(FantasyTeam fantasyTeam, Long playerId, String specialistType) throws Exception {
        Optional<FantasyPlayer> playerOpt = fantasyPlayerService.getFantasyPlayer(playerId);
        if (playerOpt.isEmpty()) {
            throw new Exception("Fantasy Player not found.");
        }

        FantasyPlayer player = playerOpt.get();

        if (!fantasyTeam.getPlayers().contains(player)) {
            throw new Exception("Player does not belong to your team.");
        }

        switch (specialistType.toUpperCase()) {
            case "PENALTY_TAKER":
                player.setPenaltyTaker(true);
                break;
            case "CORNER_TAKER":
                player.setCornerTaker(true);
                break;
            case "FREEKICK_TAKER":
                player.setFreeKickTaker(true);
                break;
            default:
                throw new Exception("Invalid specialist type.");
        }

        fantasyPlayerService.saveFantasyPlayer(player);
    }

    /**
     * Changes the team's formation.
     */
    public void changeFormation(FantasyTeam fantasyTeam, String newFormation) throws Exception {
        // Validate formation format (e.g., "4-3-3")
        if (!newFormation.matches("^\\d+-\\d+-\\d+$")) {
            throw new Exception("Invalid formation format.");
        }

        fantasyTeam.getLineup().setFormation(newFormation);
        fantasyTeamRepository.save(fantasyTeam);
    }

    /**
     * Substitutes players within the team's lineup.
     */
    public void substitutePlayers(FantasyTeam fantasyTeam, Long outPlayerId, Long inPlayerId) throws Exception {
        Optional<FantasyPlayer> outPlayerOpt = fantasyPlayerService.getFantasyPlayer(outPlayerId);
        Optional<FantasyPlayer> inPlayerOpt = fantasyPlayerService.getFantasyPlayer(inPlayerId);

        if (outPlayerOpt.isEmpty() || inPlayerOpt.isEmpty()) {
            throw new Exception("One or both Fantasy Players not found.");
        }

        FantasyPlayer outPlayer = outPlayerOpt.get();
        FantasyPlayer inPlayer = inPlayerOpt.get();

        if (!fantasyTeam.getPlayers().contains(outPlayer) || !fantasyTeam.getPlayers().contains(inPlayer)) {
            throw new Exception("One or both players do not belong to your team.");
        }

        boolean success = fantasyTeam.getLineup().substitutePlayer(outPlayer, inPlayer);
        if (!success) {
            throw new Exception("Substitution failed. Check if players are eligible.");
        }

        fantasyTeamRepository.save(fantasyTeam);
    }

    /**
        Assigns an achievement to a fantasy team.
     */
    public void assignAchievement(FantasyTeam fantasyTeam, Achievement achievement) {
        achievementService.assignAchievement(fantasyTeam, achievement);
    }

    // Additional Methods for managing lineups, formations, specialists, etc., can be added here
}
