package com.example.Player.services;

import com.example.Player.models.FantasyLeague;
import com.example.Player.models.FantasyPlayer;
import com.example.Player.models.League;
import com.example.Player.models.Player;
import com.example.Player.repository.FantasyLeagueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FantasyLeagueService {

    @Autowired
    private FantasyLeagueRepository fantasyLeagueRepository;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private FantasyPlayerService fantasyPlayerService;

    public FantasyLeague createFantasyLeague(String realLeagueCode, String fantasyLeagueName) throws Exception {
        League realLeague = leagueService.getLeagueByCode(realLeagueCode)
                .orElseThrow(() -> new Exception("Real League not found with code: " + realLeagueCode));

        if (fantasyLeagueRepository.findByRealLeague(realLeague).isPresent()) {
            throw new Exception("Fantasy League already exists for this Real League.");
        }

        FantasyLeague fantasyLeague = new FantasyLeague();
        fantasyLeague.setName(fantasyLeagueName);
        fantasyLeague.setRealLeague(realLeague);
        fantasyLeagueRepository.save(fantasyLeague);

        clonePlayers(realLeague, fantasyLeague);

        return fantasyLeague;
    }

    private void clonePlayers(League realLeague, FantasyLeague fantasyLeague) {
        List<Player> realPlayers = playerService.getPlayersByLeague(realLeague);
        for (Player realPlayer : realPlayers) {
            FantasyPlayer fantasyPlayer = new FantasyPlayer();
            fantasyPlayer.setRealPlayer(realPlayer);
            fantasyPlayer.setFantasyLeague(fantasyLeague);
            fantasyPlayer.setPrice(calculatePlayerPrice(realPlayer));
            fantasyPlayer.setStamina(100.0);
            fantasyPlayerService.saveFantasyPlayer(fantasyPlayer);
        }
    }

    private double calculatePlayerPrice(Player player) {
        // Example pricing logic based on player stats
        return (player.getOverall() + player.getPotential()) * 1000;
    }

    public Optional<FantasyLeague> getFantasyLeagueByRealLeague(League realLeague) {
        return fantasyLeagueRepository.findByRealLeague(realLeague);
    }

    // Additional methods as needed
}
