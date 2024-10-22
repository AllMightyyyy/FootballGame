package com.example.Player.mapper;

import com.example.Player.DTO.LeagueDTO;
import com.example.Player.DTO.MatchDTO;
import com.example.Player.DTO.ScoreDTO;
import com.example.Player.DTO.TeamDTO;
import com.example.Player.models.League;
import com.example.Player.models.Match;
import com.example.Player.models.Score;
import com.example.Player.models.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring")  // <-- Add this
public interface LeagueMapper {

    LeagueMapper INSTANCE = Mappers.getMapper(LeagueMapper.class);

    // Map 'league.code' to 'code' in LeagueDTO, ignoring the 'standings' field
    @Mapping(source = "code", target = "code")
    @Mapping(target = "standings", ignore = true)
    LeagueDTO leagueToLeagueDTO(League league);

    // Mapping for Team -> TeamDTO with 'league.code' to 'leagueCode' in TeamDTO
    @Mapping(source = "league.code", target = "leagueCode")
    TeamDTO teamToTeamDTO(Team team);

    // Correctly map Match -> MatchDTO and ensure the score is passed to scoreToScoreDTO
    @Mapping(source = "team1.name", target = "team1")
    @Mapping(source = "team2.name", target = "team2")
    @Mapping(source = "score", target = "score")
    @Mapping(source = "status", target = "status")
    MatchDTO matchToMatchDTO(Match match);

    // Map Score -> ScoreDTO, without referencing "match"
    @Mapping(target = "ht", expression = "java(mapHalfTimeScores(score))")
    @Mapping(target = "ft", expression = "java(mapFullTimeScores(score))")
    ScoreDTO scoreToScoreDTO(Score score);

    // Custom method to map HT scores from the Score object
    default List<Integer> mapHalfTimeScores(Score score) {
        if (score == null) {
            return Arrays.asList(null, null);
        }
        return Arrays.asList(score.getHtTeam1(), score.getHtTeam2());
    }

    // Custom method to map FT scores from the Score object
    default List<Integer> mapFullTimeScores(Score score) {
        if (score == null) {
            return Arrays.asList(null, null);
        }
        return Arrays.asList(score.getFtTeam1(), score.getFtTeam2());
    }
}
