// src/main/java/com/example/Player/services/PlayerService.java

package com.example.Player.services;

import com.example.Player.exceptions.ResourceNotFoundException;
import com.example.Player.models.League;
import com.example.Player.models.Player;
import com.example.Player.repository.PlayerRepository;
import com.example.Player.utils.PlayerSpecifications;
import com.opencsv.CSVReader;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private LeagueService leagueService;

    private static final String CSV_FILE_PATH = "players_22.csv";
    private List<Player> players = new ArrayList<>();

    @PostConstruct
    public void loadPlayersFromCSV() {
        try (CSVReader reader = new CSVReader(new FileReader(CSV_FILE_PATH))) {
            String[] line;
            reader.readNext(); // Skip header
            while ((line = reader.readNext()) != null) {
                Long id = Long.parseLong(line[0]); // sofifa_id
                if (!playerRepository.existsById(id)) {
                    Player player = new Player();
                    player.setId(id);
                    player.setShortName(line[2]);
                    player.setLongName(line[3]);
                    player.setPositions(line[4]);
                    player.setOverall(Integer.parseInt(line[5]));
                    player.setPotential(Integer.parseInt(line[6]));
                    player.setValueEur(Double.parseDouble(line[7]));
                    player.setWageEur(Double.parseDouble(line[8]));
                    player.setPlayerFaceUrl(line[105]);
                    player.setClubLogoUrl(line[106]);
                    player.setNationFlagUrl(line[109]);
                    player.setPace(Integer.parseInt(line[54]));
                    player.setShooting(Integer.parseInt(line[55]));
                    player.setPassing(Integer.parseInt(line[56]));
                    player.setDribbling(Integer.parseInt(line[57]));
                    player.setDefending(Integer.parseInt(line[58]));
                    player.setPhysical(Integer.parseInt(line[59]));
                    player.setHeightCm(Integer.parseInt(line[11]));
                    player.setWeightKg(Integer.parseInt(line[12]));
                    String leagueName = line[15].trim();
                    String clubName = line[14];
                    String nationalityName = line[23];

                    // Associate Player with League
                    Optional<League> leagueOpt = leagueService.getLeagueByName(leagueName);
                    if (leagueOpt.isPresent()) {
                        player.setLeague(leagueOpt.get());
                    } else {
                        // Optionally, handle the case where the league doesn't exist
                        // For example, skip this player or assign a default league
                        System.err.println("League not found for player: " + player.getLongName() + ", League: " + leagueName);
                        continue; // Skip saving this player
                    }

                    player.setClubName(clubName);
                    player.setNationalityName(nationalityName);
                    player.setPositionsList(Arrays.stream(line[4].split(",")).map(String::trim).collect(Collectors.toList()));

                    player.setPace(Integer.parseInt(line[54]));
                    player.setShooting(Integer.parseInt(line[55]));
                    player.setPassing(Integer.parseInt(line[56]));
                    player.setDribbling(Integer.parseInt(line[57]));
                    player.setDefending(Integer.parseInt(line[58]));
                    player.setPhysical(Integer.parseInt(line[59]));

                    playerRepository.save(player);
                }
            }
            System.out.println("Players loaded successfully from CSV.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Page<Player> searchPlayers(
            String name,
            List<String> positions,
            List<String> leagues,
            List<String> clubs,
            List<String> nations,
            int minOverall,
            int maxOverall,
            int minHeight,
            int maxHeight,
            int minWeight,
            int maxWeight,
            boolean excludePositions,
            boolean excludeLeagues,
            boolean excludeClubs,
            boolean excludeNations,
            PageRequest pageRequest
    ) {
        Specification<Player> spec = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            spec = spec.and(PlayerSpecifications.nameContains(name));
        }

        if (positions != null && !positions.isEmpty()) {
            spec = spec.and(PlayerSpecifications.positionIn(positions, excludePositions));
        }

        if (leagues != null && !leagues.isEmpty()) {
            spec = spec.and(PlayerSpecifications.leagueIn(leagues, excludeLeagues));
        }

        if (clubs != null && !clubs.isEmpty()) {
            spec = spec.and(PlayerSpecifications.clubIn(clubs, excludeClubs));
        }

        if (nations != null && !nations.isEmpty()) {
            spec = spec.and(PlayerSpecifications.nationIn(nations, excludeNations));
        }

        // Overall rating filter
        spec = spec.and(PlayerSpecifications.overallBetween(minOverall, maxOverall));

        // Height filter
        spec = spec.and(PlayerSpecifications.heightBetween(minHeight, maxHeight));

        // Weight filter
        spec = spec.and(PlayerSpecifications.weightBetween(minWeight, maxWeight));

        return playerRepository.findAll(spec, pageRequest);
    }

    public int countPlayers(
            String name,
            List<String> positions,
            List<String> leagues,
            List<String> clubs,
            List<String> nations,
            int minOverall,
            int maxOverall,
            int minHeight,
            int maxHeight,
            int minWeight,
            int maxWeight,
            boolean excludePositions,
            boolean excludeLeagues,
            boolean excludeClubs,
            boolean excludeNations
    ) {
        return (int) searchPlayers(
                name,
                positions,
                leagues,
                clubs,
                nations,
                minOverall,
                maxOverall,
                minHeight,
                maxHeight,
                minWeight,
                maxWeight,
                excludePositions,
                excludeLeagues,
                excludeClubs,
                excludeNations,
                PageRequest.of(0, Integer.MAX_VALUE)
        ).getTotalElements();
    }

    // Get player by ID
    public Player getPlayerById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with id " + id));
    }

    // Get all players (used for positions, leagues, etc.)
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }
}
