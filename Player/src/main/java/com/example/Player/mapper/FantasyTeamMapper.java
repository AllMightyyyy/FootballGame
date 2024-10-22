package com.example.Player.mapper;

import com.example.Player.DTO.FantasyTeamDTO;
import com.example.Player.DTO.TeamDTO;
import com.example.Player.DTO.FantasyPlayerDTO;
import com.example.Player.DTO.LeagueDTO;
import com.example.Player.DTO.ScoreDTO;
import com.example.Player.models.FantasyTeam;
import com.example.Player.models.Team;
import com.example.Player.models.FantasyPlayer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

@Mapper(uses = FantasyTeamMapper.class) // To enable the usage of custom methods like mapHalfTimeScores, etc.
public interface FantasyTeamMapper {
    FantasyTeamMapper INSTANCE = Mappers.getMapper(FantasyTeamMapper.class);

    // Map FantasyTeam -> FantasyTeamDTO
    @Mapping(source = "teamName", target = "teamName")
    @Mapping(source = "balance", target = "balance")
    @Mapping(source = "team", target = "team")
    @Mapping(source = "owner", target = "owner")
    @Mapping(source = "fantasyLeague", target = "fantasyLeague")
    FantasyTeamDTO toDTO(FantasyTeam fantasyTeam);

    // Mapping for Team -> TeamDTO
    TeamDTO toTeamDTO(Team team);

    // Mapping for FantasyPlayer -> FantasyPlayerDTO
    @Mapping(source = "realPlayer.longName", target = "name") // Assuming realPlayer has longName
    @Mapping(source = "realPlayer.positionsList", target = "position", qualifiedByName = "positionsListToString")
    FantasyPlayerDTO toFantasyPlayerDTO(FantasyPlayer fantasyPlayer);

    // Mapping for League -> LeagueDTO
    @Mapping(target = "standings", ignore = true)  // Ignoring standings
    LeagueDTO toLeagueDTO(com.example.Player.models.League league);

    // Mapping for Score -> ScoreDTO
    @Mapping(target = "ht", source = "score", qualifiedByName = "mapHalfTimeScores")
    @Mapping(target = "ft", source = "score", qualifiedByName = "mapFullTimeScores")
    ScoreDTO toScoreDTO(com.example.Player.models.Score score);

    // Mapping for Team -> String (used for matches)
    default String mapTeamToString(Team team) {
        return team != null ? team.getName() : null;
    }

    // Custom method to map HT and FT scores from Score object
    @Named("mapHalfTimeScores")
    default List<Integer> mapHalfTimeScores(com.example.Player.models.Score score) {
        if (score == null) {
            return Arrays.asList(null, null);
        }
        return Arrays.asList(score.getHtTeam1(), score.getHtTeam2());
    }

    @Named("mapFullTimeScores")
    default List<Integer> mapFullTimeScores(com.example.Player.models.Score score) {
        if (score == null) {
            return Arrays.asList(null, null);
        }
        return Arrays.asList(score.getFtTeam1(), score.getFtTeam2());
    }

    // Utility method to map list of positions to a single string
    @Named("positionsListToString")
    default String positionsListToString(List<String> positions) {
        return String.join(", ", positions);
    }
}
