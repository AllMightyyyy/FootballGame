// src/main/java/com/example/Player/utils/PlayerSpecifications.java

package com.example.Player.utils;

import com.example.Player.models.Player;
import com.example.Player.models.League;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class PlayerSpecifications {

    // Name contains (case insensitive)
    public static Specification<Player> nameContains(String name) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("shortName")), "%" + name.toLowerCase() + "%");
    }

    // Club filter (for inclusions or exclusions)
    public static Specification<Player> clubIn(List<String> clubs, boolean exclude) {
        if (clubs == null || clubs.isEmpty()) {
            return null;
        }

        return (root, query, builder) -> {
            if (exclude) {
                return builder.not(root.get("clubName").in(clubs));
            } else {
                return root.get("clubName").in(clubs);
            }
        };
    }

    // Nation filter (for inclusions or exclusions)
    public static Specification<Player> nationIn(List<String> nations, boolean exclude) {
        if (nations == null || nations.isEmpty()) {
            return null;
        }

        return (root, query, builder) -> {
            if (exclude) {
                return builder.not(root.get("nationalityName").in(nations));
            } else {
                return root.get("nationalityName").in(nations);
            }
        };
    }

    // Position filter (for inclusions or exclusions)
    public static Specification<Player> positionIn(List<String> positions, boolean exclude) {
        if (positions == null || positions.isEmpty()) {
            return null;
        }

        return (root, query, builder) -> {
            // Correctly join the positionsList collection
            Join<Player, String> positionsJoin = root.join("positionsList");

            if (exclude) {
                return builder.not(positionsJoin.in(positions));
            } else {
                return positionsJoin.in(positions);
            }
        };
    }

    // PlayerSpecifications.java

    public static Specification<Player> leagueIn(List<String> leagues, boolean exclude) {
        if (leagues == null || leagues.isEmpty()) {
            return null;
        }

        return (root, query, builder) -> {
            Join<Player, League> leagueJoin = root.join("league");

            if (exclude) {
                return builder.not(leagueJoin.get("name").in(leagues));
            } else {
                return leagueJoin.get("name").in(leagues);
            }
        };
    }

    public static Specification<Player> distinct() {
        return (root, query, builder) -> {
            query.distinct(true);
            return builder.conjunction();
        };
    }

    // Overall rating filter (between min and max)
    public static Specification<Player> overallBetween(int min, int max) {
        return (root, query, builder) -> builder.between(root.get("overall"), min, max);
    }

    // Height filter (between min and max)
    public static Specification<Player> heightBetween(int minHeight, int maxHeight) {
        return (root, query, builder) -> builder.between(root.get("heightCm"), minHeight, maxHeight);
    }

    // Weight filter (between min and max)
    public static Specification<Player> weightBetween(int minWeight, int maxWeight) {
        return (root, query, builder) -> builder.between(root.get("weightKg"), minWeight, maxWeight);
    }
}
