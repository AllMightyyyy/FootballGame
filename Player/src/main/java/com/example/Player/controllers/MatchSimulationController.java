// MatchSimulationController.java
package com.example.Player.controllers;

import com.example.Player.DTO.FantasyMatchSimulationRequest;
import com.example.Player.models.FantasyMatch;
import com.example.Player.services.MatchSimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/match")
public class MatchSimulationController {

    @Autowired
    private MatchSimulationService matchSimulationService;

    @PostMapping("/simulate")
    public ResponseEntity<?> simulateMatch(@RequestBody FantasyMatchSimulationRequest request) {
        try {
            matchSimulationService.simulateMatch(request);
            return ResponseEntity.ok(Map.of("message", "Match simulated successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
