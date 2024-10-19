// src/main/java/com/example/Player/controllers/PlayerController.java

package com.example.Player.controllers;

import com.example.Player.exceptions.ResourceNotFoundException;
import com.example.Player.models.League;
import com.example.Player.models.Player;
import com.example.Player.services.FormationService;
import com.example.Player.services.PlayerService;
import com.example.Player.utils.PlayerLeagueResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private FormationService formationService;

    private final Logger logger = LoggerFactory.getLogger(PlayerController.class);

    // Get players with optional filtering and pagination
    @GetMapping
    public ResponseEntity<?> searchPlayers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<String> positions,
            @RequestParam(required = false) List<String> leagues,
            @RequestParam(required = false) List<String> clubs,
            @RequestParam(required = false) List<String> nations,
            @RequestParam(defaultValue = "0") int minOverall,
            @RequestParam(defaultValue = "99") int maxOverall,
            @RequestParam(defaultValue = "150") int minHeight,
            @RequestParam(defaultValue = "220") int maxHeight,
            @RequestParam(defaultValue = "40") int minWeight,
            @RequestParam(defaultValue = "120") int maxWeight,
            @RequestParam(defaultValue = "false") boolean excludePositions,
            @RequestParam(defaultValue = "false") boolean excludeLeagues,
            @RequestParam(defaultValue = "false") boolean excludeClubs,
            @RequestParam(defaultValue = "false") boolean excludeNations,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "overall") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {
        if (minOverall > maxOverall || minHeight > maxHeight || minWeight > maxWeight) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Minimum values cannot exceed maximum values."));
        }

        if (page < 1 || size < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Page and size must be positive integers."));
        }

        try {
            PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.Direction.fromString(sortOrder), sortBy);

            Page<Player> playersPage = playerService.searchPlayers(
                    name, positions, leagues, clubs, nations,
                    minOverall, maxOverall,
                    minHeight, maxHeight,
                    minWeight, maxWeight,
                    excludePositions, excludeLeagues, excludeClubs, excludeNations,
                    pageRequest
            );

            Map<String, Object> response = new HashMap<>();
            response.put("players", playersPage.getContent());
            response.put("currentPage", playersPage.getNumber() + 1);
            response.put("totalItems", playersPage.getTotalElements());
            response.put("totalPages", playersPage.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error searching players: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred while searching players."));
        }
    }

    @GetMapping("/leagues")
    public ResponseEntity<Set<PlayerLeagueResponseDTO>> getAllLeagues() {
        Set<PlayerLeagueResponseDTO> leagues = playerService.getAllPlayers().stream()
                .map(player -> {
                    League league = player.getLeague();
                    return new PlayerLeagueResponseDTO(league.getCode(), league.getName(), league.getSeason());
                })
                .collect(Collectors.toSet());
        return ResponseEntity.ok(leagues);
    }

    @GetMapping("/clubs")
    public ResponseEntity<Set<String>> getAllClubs() {
        Set<String> clubs = playerService.getAllPlayers().stream()
                .map(Player::getClubName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(clubs);
    }

    @GetMapping("/nations")
    public ResponseEntity<Set<String>> getAllNations() {
        Set<String> nations = playerService.getAllPlayers().stream()
                .map(Player::getNationalityName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(nations);
    }

    // Get player details by ID
    @GetMapping("/{id}")
    @Cacheable("players")
    public ResponseEntity<Player> getPlayerById(@PathVariable Long id) {
        try {
            Player player = playerService.getPlayerById(id);
            return ResponseEntity.ok(player);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Add a player to the formation
    @PostMapping("/formation")
    public ResponseEntity<String> addPlayerToFormation(@RequestParam String position, @RequestParam Long playerId) {
        try {
            Player player = playerService.getPlayerById(playerId);
            if (!formationService.addPlayerToPosition(position, player)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid position or player can't play in this position");
            }
            return ResponseEntity.ok("Player added successfully");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found");
        }
    }

    // Remove a player from the formation
    @DeleteMapping("/formation")
    public ResponseEntity<String> removePlayerFromFormation(@RequestParam String position) {
        if (!formationService.removePlayerFromPosition(position)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid position or no player in this position");
        }
        return ResponseEntity.ok("Player removed successfully");
    }

    // Get the current formation
    @GetMapping("/formation")
    public ResponseEntity<Map<String, Player>> getFormation() {
        Map<String, Player> formation = formationService.getFormation();
        return ResponseEntity.ok(formation);
    }
}
