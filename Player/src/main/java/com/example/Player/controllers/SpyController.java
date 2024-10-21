package com.example.Player.controllers;

import com.example.Player.models.FantasyTeam;
import com.example.Player.models.SpyReport;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@RestController
@RequestMapping("/api/spy")
public class SpyController {

    @Autowired
    private SpyService spyService;

    @Autowired
    private FantasyTeamService fantasyTeamService;

    @PostMapping("/spy-on")
    public ResponseEntity<?> spyOnTeam(@AuthenticationPrincipal User user,
                                       @RequestParam Long targetTeamId) {
        try {
            FantasyTeam reportingTeam = fantasyTeamService.getFantasyTeamByUser(user)
                    .orElseThrow(() -> new Exception("Fantasy Team not found for user."));

            FantasyTeam targetTeam = fantasyTeamService.getFantasyTeamById(targetTeamId)
                    .orElseThrow(() -> new Exception("Target Fantasy Team not found."));

            SpyReport report = spyService.spyOnTeam(reportingTeam, targetTeam);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Additional endpoints like viewing past spy reports
}
