package com.example.Player.controllers;

import com.example.Player.DTO.FantasyLeagueCreationRequest;
import com.example.Player.DTO.FantasyLeagueDTO;
import com.example.Player.services.FantasyLeagueService;
import com.example.Player.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/fantasy-league")
public class FantasyLeagueController {

    @Autowired
    private FantasyLeagueService fantasyLeagueService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createFantasyLeague(@RequestBody FantasyLeagueCreationRequest request) {
        try {
            FantasyLeagueDTO fantasyLeagueDTO = fantasyLeagueService.createFantasyLeague(request.getRealLeagueCode(), request.getFantasyLeagueName());
            return ResponseEntity.ok(fantasyLeagueDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Additional endpoints like fetching fantasy leagues, etc.
}
