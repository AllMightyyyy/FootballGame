package com.example.Player.services;

import com.example.Player.models.Player;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PlayerService {

    private static final String CSV_FILE_PATH = "players_22.csv";
    private List<Player> players;

    public PlayerService() {
        this.players = loadPlayersFromCSV();
    }

    private double parseDoubleSafe(String value, double defaultValue) {
        try {
            if (value == null || value.isEmpty()) {
                return defaultValue;
            }
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private int parseIntSafe(String value, int defaultValue) {
        try {
            if (value == null || value.isEmpty()) {
                return defaultValue;
            }
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // Load players once and cache them in memory for faster access
    private List<Player> loadPlayersFromCSV() {
        List<Player> players = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(CSV_FILE_PATH))) {
            String[] line;
            reader.readNext(); // Skip header
            while ((line = reader.readNext()) != null) {
                Player player = new Player();
                player.setId(Long.parseLong(line[0])); // sofifa_id
                player.setShortName(line[2]); // short_name
                player.setLongName(line[3]); // long_name
                player.setPositions(line[4]); // player_positions
                player.setOverall(parseIntSafe(line[5], 0)); // overall
                player.setPotential(parseIntSafe(line[6], 0)); // potential
                player.setValueEur(parseDoubleSafe(line[7], 0.0)); // value_eur
                player.setWageEur(parseDoubleSafe(line[8], 0.0)); // wage_eur
                player.setPlayerFaceUrl(line[105]); // player_face_url
                player.setClubLogoUrl(line[106]); // club_logo_url
                player.setNationFlagUrl(line[109]); // nation_flag_url

                // Set the new attribute fields
                player.setPace(parseIntSafe(line[54], 0)); // pace
                player.setShooting(parseIntSafe(line[55], 0)); // shooting
                player.setPassing(parseIntSafe(line[56], 0)); // passing
                player.setDribbling(parseIntSafe(line[57], 0)); // dribbling
                player.setDefending(parseIntSafe(line[58], 0)); // defending
                player.setPhysical(parseIntSafe(line[59], 0)); // physical

                player.setHeightCm(parseIntSafe(line[11], 0)); // height_cm
                player.setWeightKg(parseIntSafe(line[12], 0)); // weight_kg
                player.setLeagueName(line[15]); // league_name
                player.setClubName(line[14]);   // club_name
                player.setNationalityName(line[23]); // nationality_name

                player.setPositionsList(Arrays.stream(line[4].split(","))
                        .map(String::trim)
                        .collect(Collectors.toList()));

                players.add(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return players;
    }

    // Basic search and filtering logic
    public List<Player> searchPlayers(
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
            int page,
            int size,
            String sortBy,
            String sortOrder
    ) {
        Stream<Player> stream = players.stream();

        // Name filter
        if (name != null && !name.isEmpty()) {
            stream = stream.filter(player -> player.getShortName().toLowerCase().contains(name.toLowerCase()));
        }

        // Position filter
        if (positions != null && !positions.isEmpty()) {
            if (excludePositions) {
                stream = stream.filter(player -> player.getPositionsList().stream().noneMatch(positions::contains));
            } else {
                stream = stream.filter(player -> player.getPositionsList().stream().anyMatch(positions::contains));
            }
        }

        // League filter
        if (leagues != null && !leagues.isEmpty()) {
            if (excludeLeagues) {
                stream = stream.filter(player -> !leagues.contains(player.getLeagueName()));
            } else {
                stream = stream.filter(player -> leagues.contains(player.getLeagueName()));
            }
        }

        // Club filter
        if (clubs != null && !clubs.isEmpty()) {
            if (excludeClubs) {
                stream = stream.filter(player -> !clubs.contains(player.getClubName()));
            } else {
                stream = stream.filter(player -> clubs.contains(player.getClubName()));
            }
        }

        // Nation filter
        if (nations != null && !nations.isEmpty()) {
            if (excludeNations) {
                stream = stream.filter(player -> !nations.contains(player.getNationalityName()));
            } else {
                stream = stream.filter(player -> nations.contains(player.getNationalityName()));
            }
        }

        // Overall filter
        stream = stream.filter(player -> player.getOverall() >= minOverall && player.getOverall() <= maxOverall);

        // Height filter
        stream = stream.filter(player -> player.getHeightCm() >= minHeight && player.getHeightCm() <= maxHeight);

        // Weight filter
        stream = stream.filter(player -> player.getWeightKg() >= minWeight && player.getWeightKg() <= maxWeight);

        // Sorting
        Comparator<Player> comparator = getComparator(sortBy, sortOrder);
        stream = stream.sorted(comparator);

        // Pagination
        List<Player> filteredPlayers = stream
                .skip((page - 1) * size)
                .limit(size)
                .collect(Collectors.toList());

        return filteredPlayers;
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
        Stream<Player> stream = players.stream();

        // Name filter
        if (name != null && !name.isEmpty()) {
            stream = stream.filter(player -> player.getShortName().toLowerCase().contains(name.toLowerCase()));
        }

        // Position filter
        if (positions != null && !positions.isEmpty()) {
            if (excludePositions) {
                stream = stream.filter(player -> player.getPositionsList().stream().noneMatch(positions::contains));
            } else {
                stream = stream.filter(player -> player.getPositionsList().stream().anyMatch(positions::contains));
            }
        }

        // League filter
        if (leagues != null && !leagues.isEmpty()) {
            if (excludeLeagues) {
                stream = stream.filter(player -> !leagues.contains(player.getLeagueName()));
            } else {
                stream = stream.filter(player -> leagues.contains(player.getLeagueName()));
            }
        }

        // Club filter
        if (clubs != null && !clubs.isEmpty()) {
            if (excludeClubs) {
                stream = stream.filter(player -> !clubs.contains(player.getClubName()));
            } else {
                stream = stream.filter(player -> clubs.contains(player.getClubName()));
            }
        }

        // Nation filter
        if (nations != null && !nations.isEmpty()) {
            if (excludeNations) {
                stream = stream.filter(player -> !nations.contains(player.getNationalityName()));
            } else {
                stream = stream.filter(player -> nations.contains(player.getNationalityName()));
            }
        }

        // Overall filter
        stream = stream.filter(player -> player.getOverall() >= minOverall && player.getOverall() <= maxOverall);

        // Height filter
        stream = stream.filter(player -> player.getHeightCm() >= minHeight && player.getHeightCm() <= maxHeight);

        // Weight filter
        stream = stream.filter(player -> player.getWeightKg() >= minWeight && player.getWeightKg() <= maxWeight);

        return (int) stream.count();
    }


    // Sort logic (sort by a field in asc or desc order)
    private Comparator<Player> getComparator(String sortBy, String sortDirection) {
        Comparator<Player> comparator = Comparator.comparing(Player::getShortName); // Default sorting
        if (sortBy.equals("overall")) {
            comparator = Comparator.comparing(Player::getOverall);
        } else if (sortBy.equals("potential")) {
            comparator = Comparator.comparing(Player::getPotential);
        }
        return sortDirection.equals("desc") ? comparator.reversed() : comparator;
    }

    // Get a specific player by ID
    public Player getPlayerById(Long id) {
        return players.stream()
                .filter(player -> player.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Player> getAllPlayers() {
        return players;
    }
}
