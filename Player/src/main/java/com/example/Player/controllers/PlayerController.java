package com.example.Player.controllers;

import com.example.Player.models.Player;
import com.example.Player.services.FormationService;
import com.example.Player.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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

    // Get players with optional filtering and pagination
    // Modify the controller
    @GetMapping
    public ResponseEntity<Map<String, Object>> searchPlayers(
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (page < 1 || size < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        List<Player> players = playerService.searchPlayers(
                name, positions, leagues, clubs, nations,
                minOverall, maxOverall,
                minHeight, maxHeight,
                minWeight, maxWeight,
                excludePositions, excludeLeagues, excludeClubs, excludeNations,
                page, size, sortBy, sortOrder
        );
        int totalItems = playerService.countPlayers(
                name, positions, leagues, clubs, nations,
                minOverall, maxOverall,
                minHeight, maxHeight,
                minWeight, maxWeight,
                excludePositions, excludeLeagues, excludeClubs, excludeNations
        );

        Map<String, Object> response = new HashMap<>();
        response.put("players", players);
        response.put("currentPage", page);
        response.put("totalItems", totalItems);
        response.put("totalPages", (int) Math.ceil((double) totalItems / size));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/positions")
    public ResponseEntity<Set<String>> getAllPositions() {
        Set<String> positions = playerService.getAllPlayers().stream()
                .flatMap(player -> player.getPositionsList().stream())
                .collect(Collectors.toSet());
        return ResponseEntity.ok(positions);
    }

    @GetMapping("/leagues")
    public ResponseEntity<Set<String>> getAllLeagues() {
        Set<String> leagues = playerService.getAllPlayers().stream()
                .map(Player::getLeagueName)
                .filter(Objects::nonNull)
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
        Player player = playerService.getPlayerById(id);
        if (player == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(player);
    }

    // Add a player to the formation
    @PostMapping("/formation")
    public ResponseEntity<String> addPlayerToFormation(@RequestParam String position, @RequestParam Long playerId) {
        Player player = playerService.getPlayerById(playerId);
        if (player == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found");
        }
        if (!formationService.addPlayerToPosition(position, player)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid position or player can't play in this position");
        }
        return ResponseEntity.ok("Player added successfully");
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
