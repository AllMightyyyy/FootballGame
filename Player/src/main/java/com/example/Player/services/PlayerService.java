// src/main/java/com/example/Player/services/PlayerService.java

package com.example.Player.services;

import com.example.Player.DTO.PlayerDTO;
import com.example.Player.exceptions.DuplicatePlayerException;
import com.example.Player.exceptions.ResourceNotFoundException;
import com.example.Player.models.League;
import com.example.Player.models.Player;
import com.example.Player.repository.PlayerRepository;
import com.example.Player.utils.PlayerSpecifications;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private LeagueService leagueService;

    private static final String CSV_FILE_PATH = "players_22.csv";

    private final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    /**
     * Loads players from the CSV file into the database, ensuring uniqueness.
     */
    @Transactional
    public void loadPlayersFromCSV() {
        File csvFile = new File(CSV_FILE_PATH);
        if (!csvFile.exists()) {
            logger.error("CSV file not found at path: {}", CSV_FILE_PATH);
            return;
        }

        int lineNumber = 0;
        int duplicatesSkipped = 0;
        int playersLoaded = 0;

        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(new FileInputStream(csvFile), StandardCharsets.UTF_8))
                .withSkipLines(1) // skip header
                .build()) {

            String[] headers = getCSVHeaders();
            logger.info("CSV Headers: {}", Arrays.toString(headers));
            String[] line;

            while ((line = reader.readNext()) != null) {
                lineNumber++;
                try {
                    Map<String, String> playerData = mapColumns(headers, line);

                    // Validate required fields with enhanced logging
                    if (!isValidPlayerData(playerData, lineNumber)) {
                        continue;
                    }

                    // Extract unique identifiers
                    String longName = playerData.get("long_name").trim();
                    String clubName = playerData.get("club_name").trim();
                    String nationalityName = playerData.get("nationality_name").trim();

                    // Check for duplicate based on unique fields
                    boolean exists = playerRepository.existsByLongNameAndClubNameAndNationalityName(
                            longName,
                            clubName,
                            nationalityName
                    );

                    if (exists) {
                        logger.info("Line {}: Duplicate player found ({}). Skipping.", lineNumber + 1, longName);
                        duplicatesSkipped++;
                        continue;
                    }

                    // Create and populate the Player entity
                    Player player = new Player();

                    // Parse and set sofifa_id
                    String sofifaIdStr = playerData.get("sofifa_id").trim();
                    try {
                        player.setId(Long.parseLong(sofifaIdStr));
                    } catch (NumberFormatException e) {
                        logger.warn("Line {}: Invalid sofifa_id '{}'. Skipping.", lineNumber + 1, sofifaIdStr);
                        continue;
                    }

                    player.setShortName(playerData.get("short_name").trim());
                    player.setLongName(longName);
                    player.setPositions(playerData.get("player_positions").trim());

                    // Parse and set numerical fields
                    player.setOverall(parseInteger(playerData.get("overall"), lineNumber + 1, "overall"));
                    player.setPotential(parseInteger(playerData.get("potential"), lineNumber + 1, "potential"));
                    player.setValueEur(parseDouble(playerData.get("value_eur"), lineNumber + 1, "value_eur"));
                    player.setWageEur(parseDouble(playerData.get("wage_eur"), lineNumber + 1, "wage_eur"));
                    player.setPace(parseInteger(playerData.get("pace"), lineNumber + 1, "pace"));
                    player.setShooting(parseInteger(playerData.get("shooting"), lineNumber + 1, "shooting"));
                    player.setPassing(parseInteger(playerData.get("passing"), lineNumber + 1, "passing"));
                    player.setDribbling(parseInteger(playerData.get("dribbling"), lineNumber + 1, "dribbling"));
                    player.setDefending(parseInteger(playerData.get("defending"), lineNumber + 1, "defending"));
                    player.setPhysical(parseInteger(playerData.get("physic"), lineNumber + 1, "physic"));
                    player.setHeightCm(parseInteger(playerData.get("height_cm"), lineNumber + 1, "height_cm"));
                    player.setWeightKg(parseInteger(playerData.get("weight_kg"), lineNumber + 1, "weight_kg"));

                    player.setGoalkeepingDiving(parseInteger(playerData.get("goalkeeping_diving"), lineNumber + 1, "goalkeeping_diving"));
                    player.setGoalkeepingHandling(parseInteger(playerData.get("goalkeeping_handling"), lineNumber + 1, "goalkeeping_handling"));
                    player.setGoalkeepingKicking(parseInteger(playerData.get("goalkeeping_kicking"), lineNumber + 1, "goalkeeping_kicking"));
                    player.setGoalkeepingPositioning(parseInteger(playerData.get("goalkeeping_positioning"), lineNumber + 1, "goalkeeping_positioning"));
                    player.setGoalkeepingReflexes(parseInteger(playerData.get("goalkeeping_reflexes"), lineNumber + 1, "goalkeeping_reflexes"));
                    player.setGoalkeepingSpeed(parseInteger(playerData.get("goalkeeping_speed"), lineNumber + 1, "goalkeeping_speed"));

                    // Set URLs, handling nulls
                    player.setPlayerFaceUrl(playerData.getOrDefault("player_face_url", "").trim());
                    player.setClubLogoUrl(playerData.getOrDefault("club_logo_url", "").trim());
                    player.setNationFlagUrl(playerData.getOrDefault("nation_flag_url", "").trim());

                    player.setClubName(clubName);
                    player.setNationalityName(nationalityName);

                    // Handle positions list with expansion
                    List<String> positionsList = expandPositions(Arrays.stream(player.getPositions().split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.toList()));
                    player.setPositionsList(positionsList);

                    // Assign League
                    String leagueName = playerData.get("league_name").trim();
                    String leagueCode = LEAGUE_NAME_TO_CODE_MAP.get(leagueName);

                    if (leagueCode == null) {
                        logger.warn("Line {}: Unknown league name '{}'. Skipping.", lineNumber + 1, leagueName);
                        continue; // Skip this player as league_code cannot be determined
                    }

                    League league = leagueService.getLeagueByCode(leagueCode)
                            .orElseThrow(() -> new IllegalArgumentException("League not found with code: " + leagueCode));
                    player.setLeague(league);

                    // Save the player using the application-level save method to enforce uniqueness
                    savePlayer(player);
                    playersLoaded++;

                } catch (DuplicatePlayerException e) {
                    logger.warn("Line {}: {}", lineNumber + 1, e.getMessage());
                    duplicatesSkipped++;
                } catch (Exception e) {
                    logger.error("Line {}: Unexpected error - {}", lineNumber + 1, e.getMessage());
                }
            }

            logger.info("Player CSV Import Completed. Total Records: {}, Players Loaded: {}, Duplicates Skipped: {}",
                    lineNumber, playersLoaded, duplicatesSkipped);

        } catch (Exception e) {
            logger.error("Failed to load players from CSV: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Saves a player after ensuring uniqueness based on long_name, club_name, and nationality_name.
     */
    public Player savePlayer(Player player) throws DuplicatePlayerException {
        boolean exists = playerRepository.existsByLongNameAndClubNameAndNationalityName(
                player.getLongName(),
                player.getClubName(),
                player.getNationalityName()
        );

        if (exists) {
            throw new DuplicatePlayerException("Player already exists with the same name, club, and nationality.");
        }

        return playerRepository.save(player);
    }

    /**
     * Retrieves CSV headers by reading the first line of the CSV.
     */
    private String[] getCSVHeaders() throws Exception {
        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(CSV_FILE_PATH), StandardCharsets.UTF_8))) {
            return reader.readNext();
        }
    }

    /**
     * Maps CSV columns to a key-value pair based on headers.
     * Ensures that all headers are present in the map, assigning empty strings for missing values.
     */
    private Map<String, String> mapColumns(String[] headers, String[] line) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            String key = headers[i].trim().toLowerCase().replace(" ", "_"); // Normalize header
            String value = (i < line.length) ? line[i].trim() : ""; // Assign empty string if missing
            map.put(key, value);
        }
        return map;
    }

    /**
     * Validates if the player data contains all required fields.
     * Only checks for essential fields necessary for adding a player.
     */
    private boolean isValidPlayerData(Map<String, String> playerData, int lineNumber) {
        List<String> requiredFields = Arrays.asList(
                "sofifa_id",
                "long_name",
                "player_positions",
                "player_face_url",
                "club_logo_url",
                "club_name",
                "nationality_name",
                "league_name" // Changed from league_code to league_name
        );

        List<String> missingFields = requiredFields.stream()
                .filter(field -> !playerData.containsKey(field) || playerData.get(field).isEmpty())
                .collect(Collectors.toList());

        if (!missingFields.isEmpty()) {
            logger.warn("Line {}: Missing or empty fields {}. Skipping.", lineNumber + 1, missingFields);
            return false;
        }
        return true;
    }

    private static final Map<String, String> LEAGUE_NAME_TO_CODE_MAP = Map.of(
            "English Premier League", "en.1",
            "French Ligue 1", "fr.1",
            "German 1. Bundesliga", "de.1",
            "Spain Primera Division", "es.1",
            "Italian Serie A", "it.1"
            // Add other league mappings here
    );


    /**
     * Parses a string to integer with default value handling.
     */
    private int parseInteger(String value, int lineNumber, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.warn("Line {}: Invalid integer for field '{}', defaulting to 0.", lineNumber, fieldName);
            return 0;
        }
    }

    /**
     * Parses a string to double with default value handling.
     */
    private double parseDouble(String value, int lineNumber, String fieldName) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            logger.warn("Line {}: Invalid double for field '{}', defaulting to 0.0.", lineNumber, fieldName);
            return 0.0;
        }
    }

    /**
     * Expands certain positions automatically.
     */
    private List<String> expandPositions(List<String> originalPositions) {
        List<String> expandedPositions = new ArrayList<>(originalPositions);

        for (String pos : originalPositions) {
            if (pos.equalsIgnoreCase("CM")) {
                if (!expandedPositions.contains("LCM")) {
                    expandedPositions.add("LCM");
                }
                if (!expandedPositions.contains("RCM")) {
                    expandedPositions.add("RCM");
                }
            }

            if (pos.equalsIgnoreCase("CB")) {
                if (!expandedPositions.contains("LCB")) {
                    expandedPositions.add("LCB");
                }
                if (!expandedPositions.contains("RCB")) {
                    expandedPositions.add("RCB");
                }
            }
        }

        return expandedPositions.stream()
                .distinct()
                .collect(Collectors.toList());
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

        // Execute the query with Specification and Pagination
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
        dto.setGoalkeepingDiving(player.getGoalkeepingDiving());
        dto.setGoalkeepingHandling(player.getGoalkeepingHandling());
        dto.setGoalkeepingKicking(player.getGoalkeepingKicking());
        dto.setGoalkeepingPositioning(player.getGoalkeepingPositioning());
        dto.setGoalkeepingReflexes(player.getGoalkeepingReflexes());
        dto.setGoalkeepingSpeed(player.getGoalkeepingSpeed());


        // Map League details
        if (player.getLeague() != null) {
            dto.setLeagueName(player.getLeague().getName());
            dto.setLeagueCode(player.getLeague().getCode());
        }

        return dto;
    }

    public List<Player> getPlayersByLeague(League league) {
        // Fetch players by the league, assuming players have a reference to the league
        return playerRepository.findAll().stream()
                .filter(player -> league.equals(player.getLeague()))
                .collect(Collectors.toList());
    }

}
