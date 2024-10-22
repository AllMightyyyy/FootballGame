package com.example.Player.controllers;

import com.example.Player.DTO.FantasyLeagueCreationRequest;
import com.example.Player.DTO.FantasyLeagueDTO;
import com.example.Player.services.FantasyLeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/fantasy-league")
public class FantasyLeagueController {

    @Autowired
    private FantasyLeagueService fantasyLeagueService;

    @PostMapping("/create")
    public ResponseEntity<FantasyLeagueDTO> createFantasyLeague(@Valid @RequestBody FantasyLeagueCreationRequest request) throws Exception {
        FantasyLeagueDTO fantasyLeagueDTO = fantasyLeagueService.createFantasyLeague(request.getRealLeagueCode(), request.getFantasyLeagueName());
        return ResponseEntity.ok(fantasyLeagueDTO);
    }

    // Additional endpoints like fetching fantasy leagues, etc.
}
