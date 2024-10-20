// src/main/java/com/example/Player/services/PlayerService.java

package com.example.Player.services;

import com.example.Player.exceptions.ResourceNotFoundException;
import com.example.Player.models.League;
import com.example.Player.models.Player;
import com.example.Player.repository.PlayerRepository;
import com.example.Player.utils.LeagueConfig;
import com.example.Player.DTO.PlayerDTO;
import com.example.Player.utils.PlayerSpecifications;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private LeagueConfig leagueConfig;

    private static final String CSV_FILE_PATH = "players_22.csv";

    private final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    /**
     * Loads players from the CSV file into the database.
     * This method can be called on application startup using @PostConstruct if desired.
     */
    @Transactional
    public void loadPlayersFromCSV() {
        File csvFile = new File(CSV_FILE_PATH);
        if (!csvFile.exists()) {
            logger.error("CSV file not found at path: {}", CSV_FILE_PATH);
            return;
        }

        // Use CSVReader to read the file from the specified location
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(csvFile))
                .withSkipLines(1) // skip header
                .build()) {

            String[] headers = getCSVHeaders();
            String[] line;
            int lineNumber = 1;

            while ((line = reader.readNext()) != null) {
                lineNumber++;
                try {
                    Map<String, String> playerData = mapColumns(headers, line);

                    // Validate required fields
                    if (!isValidPlayerData(playerData)) {
                        //logger.warn("Line {}: Missing required fields. Skipping.", lineNumber);
                        continue;
                    }

                    Long id = Long.parseLong(playerData.get("sofifa_id"));

                    if (playerRepository.existsById(id)) {
                        //logger.info("Line {}: Player with ID {} already exists. Skipping.", lineNumber, id);
                        continue;
                    }

                    Player player = new Player();
                    player.setId(id);
                    player.setShortName(playerData.get("short_name"));
                    player.setLongName(playerData.get("long_name"));
                    player.setPositions(playerData.get("player_positions"));
                    player.setOverall(parseInteger(playerData.get("overall"), lineNumber, "overall"));
                    player.setPotential(parseInteger(playerData.get("potential"), lineNumber, "potential"));
                    player.setValueEur(parseDouble(playerData.get("value_eur"), lineNumber, "value_eur"));
                    player.setWageEur(parseDouble(playerData.get("wage_eur"), lineNumber, "wage_eur"));

                    // Handle URLs with defaults
                    player.setPlayerFaceUrl(Optional.ofNullable(playerData.get("player_face_url")).orElse(""));
                    player.setClubLogoUrl(Optional.ofNullable(playerData.get("club_logo_url")).orElse(""));
                    player.setNationFlagUrl(Optional.ofNullable(playerData.get("nation_flag_url")).orElse(""));


                    // Handle numeric fields with validation
                    player.setPace(parseInteger(playerData.get("pace"), lineNumber, "pace"));
                    player.setShooting(parseInteger(playerData.get("shooting"), lineNumber, "shooting"));
                    player.setPassing(parseInteger(playerData.get("passing"), lineNumber, "passing"));
                    player.setDribbling(parseInteger(playerData.get("dribbling"), lineNumber, "dribbling"));
                    player.setDefending(parseInteger(playerData.get("defending"), lineNumber, "defending"));
                    player.setPhysical(parseInteger(playerData.get("physic"), lineNumber, "physic"));

                    player.setHeightCm(parseInteger(playerData.get("height_cm"), lineNumber, "height_cm"));
                    player.setWeightKg(parseInteger(playerData.get("weight_kg"), lineNumber, "weight_kg"));

                    //String csvLeagueCode = playerData.get("league_code").trim(); // Assuming CSV has league_code

                    String csvLeagueName = playerData.get("league_name").trim();
                    String csvLeagueCode = getLeagueCodeByName(csvLeagueName);
                    if (csvLeagueCode == null) {
                        //logger.warn("Line {}: League '{}' not found in configuration for player '{}'. Skipping.", lineNumber, csvLeagueName, player.getLongName());
                        continue;
                    }
                    LeagueConfig.LeagueDetails leagueDetails = leagueConfig.getLeagues().get(csvLeagueCode);

                    String season = leagueDetails.getSeason();


                    // Fetch League by code
                    Optional<League> leagueOpt = leagueService.getLeagueByCode(csvLeagueCode);
                    if (leagueOpt.isPresent()) {
                        player.setLeague(leagueOpt.get());
                    } else {
                        //logger.warn("Line {}: League with code '{}' not found for player '{}'. Skipping.", lineNumber, csvLeagueCode, player.getLongName());
                        continue; // Skip saving this player
                    }

                    String clubName = playerData.get("club_name");
                    String nationalityName = playerData.get("nationality_name");

                    player.setClubName(clubName);
                    player.setNationalityName(nationalityName);
                    player.setPositionsList(Arrays.stream(player.getPositions().split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.toList()));

                    // Save player to repository
                    playerRepository.save(player);
                    //logger.info("Line {}: Loaded player '{}'.", lineNumber, player.getLongName());

                } catch (NumberFormatException e) {
                    //logger.error("Line {}: Number format error - {}", lineNumber, e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    //logger.error("Line {}: Array index out of bounds - {}", lineNumber, e.getMessage());
                } catch (Exception e) {
                    //logger.error("Line {}: Unexpected error - {}", lineNumber, e.getMessage());
                }
            }
            logger.info("Players loaded successfully from CSV.");
        } catch (Exception e) {
            logger.error("Failed to load players from CSV: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper method to read CSV headers
     */
    private String[] getCSVHeaders() throws Exception {
        // Open the file again to read the first row (header)
        try (CSVReader reader = new CSVReader(new FileReader(CSV_FILE_PATH))) {
            return reader.readNext();
        }
    }

    /**
     * Maps CSV columns to a key-value pair based on headers
     */
    private Map<String, String> mapColumns(String[] headers, String[] line) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < headers.length && i < line.length; i++) {
            map.put(headers[i], line[i].trim());
        }
        return map;
    }

    /**
     * Validates if the player data contains all required fields
     */
    private boolean isValidPlayerData(Map<String, String> playerData) {
        return playerData.containsKey("sofifa_id") &&
                playerData.containsKey("short_name") &&
                playerData.containsKey("long_name") &&
                playerData.containsKey("player_positions") &&
                playerData.containsKey("league_name") && // Ensure league_code is present
                playerData.containsKey("club_name") &&
                playerData.containsKey("nationality_name");
    }

    /**
     * Parses a string to integer with default value handling
     */
    private int parseInteger(String value, int lineNumber, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            //logger.warn("Line {}: Invalid integer for field '{}', defaulting to 0.", lineNumber, fieldName);
            return 0;
        }
    }

    private String getLeagueCodeByName(String leagueName) {
        return leagueConfig.getLeagues().values().stream()
                .filter(details -> details.getName().equalsIgnoreCase(leagueName))
                .map(LeagueConfig.LeagueDetails::getCode)
                .findFirst()
                .orElse(null);
    }

    /**
     * Parses a string to double with default value handling
     */
    private double parseDouble(String value, int lineNumber, String fieldName) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            //logger.warn("Line {}: Invalid double for field '{}', defaulting to 0.0.", lineNumber, fieldName);
            return 0.0;
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

        //spec = spec.and(PlayerSpecifications.distinct());

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

    public PlayerDTO convertToPlayerDTO(Player player) {
        PlayerDTO dto = new PlayerDTO();

        dto.setId(player.getId());
        dto.setShortName(player.getShortName());
        dto.setLongName(player.getLongName());
        dto.setPositions(player.getPositions());
        dto.setPositionsList(player.getPositionsList());
        dto.setOverall(player.getOverall());
        dto.setPotential(player.getPotential());
        dto.setValueEur(player.getValueEur());
        dto.setWageEur(player.getWageEur());
        dto.setPlayerFaceUrl(player.getPlayerFaceUrl());
        dto.setClubLogoUrl(player.getClubLogoUrl());
        dto.setNationFlagUrl(player.getNationFlagUrl());
        dto.setClubName(player.getClubName());
        dto.setNationalityName(player.getNationalityName());
        dto.setHeightCm(player.getHeightCm());
        dto.setWeightKg(player.getWeightKg());
        dto.setPace(player.getPace());
        dto.setShooting(player.getShooting());
        dto.setPassing(player.getPassing());
        dto.setDribbling(player.getDribbling());
        dto.setDefending(player.getDefending());
        dto.setPhysical(player.getPhysical());

        // Map League details
        if (player.getLeague() != null) {
            dto.setLeagueName(player.getLeague().getName());
            dto.setLeagueCode(player.getLeague().getCode());
        }

        return dto;
    }

}
