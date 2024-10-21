package com.example.Player.services;

import com.example.Player.models.FantasyPlayer;
import com.example.Player.models.FantasyTeam;
import com.example.Player.models.SpyReport;
import com.example.Player.models.Tactics;
import com.example.Player.repository.SpyReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SpyService {

    @Autowired
    private SpyReportRepository spyReportRepository;

    @Autowired
    private FantasyTeamService fantasyTeamService;

    @Autowired
    private TacticsService tacticsService;

    @Autowired
    private FantasyPlayerService fantasyPlayerService;

    public SpyReport spyOnTeam(FantasyTeam reportingTeam, FantasyTeam targetTeam) {
        // Fetch tactics and player statuses
        Tactics targetTactics = fetchTeamTactics(targetTeam);
        String playerStatus = fetchPlayerStatuses(targetTeam);

        SpyReport report = new SpyReport();
        report.setReportingTeam(reportingTeam);
        report.setTargetTeam(targetTeam);
        report.setTactics(targetTactics.toString());
        report.setPlayerStatus(playerStatus);
        report.setReportedAt(LocalDateTime.now());

        spyReportRepository.save(report);
        return report;
    }

    private Tactics fetchTeamTactics(FantasyTeam team) {
        // Implement logic to retrieve team's current tactics
        return team.getCurrentTactics();
    }

    private String fetchPlayerStatuses(FantasyTeam team) {
        // Implement logic to serialize player stats and stamina
        StringBuilder status = new StringBuilder();
        for (FantasyPlayer player : team.getPlayers()) {
            status.append(player.getRealPlayer().getName())
                    .append(" - Stamina: ").append(player.getStamina())
                    .append(", Overall: ").append(player.getRealPlayer().getOverall())
                    .append("\n");
        }
        return status.toString();
    }
}
