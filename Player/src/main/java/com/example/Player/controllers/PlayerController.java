package com.example.Player.controllers;

import com.example.Player.models.Player;
import com.example.Player.services.FormationService;
import com.example.Player.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            @RequestParam(required = false) String position,
            @RequestParam(defaultValue = "0") int minOverall,
            @RequestParam(defaultValue = "99") int maxOverall,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "overall") String sortBy, // default sorting by overall
            @RequestParam(defaultValue = "asc") String sortOrder // default sorting order
    ) {
        if (minOverall > maxOverall) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Handle invalid input
        }
        if (page < 1 || size < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Ensure pagination is valid
        }

        List<Player> players = playerService.searchPlayers(name, position, minOverall, maxOverall, page, size, sortBy, sortOrder);
        int totalItems = playerService.countPlayers(name, position, minOverall, maxOverall);

        Map<String, Object> response = new HashMap<>();
        response.put("players", players);
        response.put("currentPage", page);
        response.put("totalItems", totalItems);
        response.put("totalPages", (int) Math.ceil((double) totalItems / size));

        return ResponseEntity.ok(response);
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
