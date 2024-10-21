package com.example.Player.services;

import com.example.Player.DTO.FantasyMatchSimulationRequest;
import com.example.Player.models.FantasyTeam;
import com.example.Player.models.FriendlyMatch;
import com.example.Player.repository.FriendlyMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FriendlyMatchService {

    @Autowired
    private FriendlyMatchRepository friendlyMatchRepository;

    @Autowired
    private FantasyMatchService fantasyMatchService;

    @Autowired
    private FantasyTeamService fantasyTeamService;

    public FriendlyMatch requestFriendlyMatch(FantasyTeam requester, FantasyTeam target) {
        FriendlyMatch friendlyMatch = new FriendlyMatch();
        friendlyMatch.setTeam1(requester);
        friendlyMatch.setTeam2(target);
        friendlyMatch.setStatus("REQUESTED");
        friendlyMatch.setRequestedAt(LocalDateTime.now());
        friendlyMatchRepository.save(friendlyMatch);
        return friendlyMatch;
    }

    public void acceptFriendlyMatch(Long matchId) throws Exception {
        FriendlyMatch friendlyMatch = friendlyMatchRepository.findById(matchId)
                .orElseThrow(() -> new Exception("Friendly Match not found."));

        if (!friendlyMatch.getStatus().equals("REQUESTED")) {
            throw new Exception("Friendly Match cannot be accepted.");
        }

        friendlyMatch.setStatus("ACCEPTED");
        friendlyMatch.setScheduledAt(LocalDateTime.now().plusDays(1)); // Schedule for next day
        friendlyMatchRepository.save(friendlyMatch);
    }

    public void simulateFriendlyMatch(Long matchId) throws Exception {
        FriendlyMatch friendlyMatch = friendlyMatchRepository.findById(matchId)
                .orElseThrow(() -> new Exception("Friendly Match not found."));

        if (!friendlyMatch.getStatus().equals("ACCEPTED")) {
            throw new Exception("Friendly Match is not ready for simulation.");
        }

        FantasyMatchSimulationRequest request = new FantasyMatchSimulationRequest();
        request.setRealLeague(friendlyMatch.getTeam1().getFantasyLeague().getRealLeague());
        request.setUser1(friendlyMatch.getTeam1().getOwner());
        request.setUser2(friendlyMatch.getTeam2().getOwner());
        request.setTactics1(friendlyMatch.getTeam1().getCurrentTactics());
        request.setTactics2(friendlyMatch.getTeam2().getCurrentTactics());
        request.setReferee(refereeService.assignRefereeToMatch());

        matchSimulationService.simulateMatch(request);

        friendlyMatch.setStatus("COMPLETED");
        friendlyMatchRepository.save(friendlyMatch);
    }

    // Additional methods like declineFriendlyMatch, viewFriendlyMatches, etc.
}
