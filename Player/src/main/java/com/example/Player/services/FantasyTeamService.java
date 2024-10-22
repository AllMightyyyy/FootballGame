// FantasyTeamService.java
package com.example.Player.services;

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

    /**
     * Assigns a fantasy team to a user.
     */
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

        // Initialize stadium, sponsors, objectives, lineup, etc., as needed
        initializeFantasyTeamComponents(fantasyTeam);

        fantasyTeamRepository.save(fantasyTeam);

        return fantasyTeam;
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
        stadium.setFantasyTeam(fantasyTeam);
        stadiumRepository.save(stadium);
        fantasyTeam.setStadium(stadium);

        // Initialize sponsors (optional)
        // Example: Assign default sponsors or leave empty

        // Assign objectives based on team stature
        // For demonstration, assign a generic objective
        objectiveService.assignObjective(fantasyTeam, "Win the League");

        // Initialize lineup with default formation
        Lineup lineup = new Lineup();
        lineup.setFormation("4-3-3");
        fantasyTeam.setLineup(lineup);
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
