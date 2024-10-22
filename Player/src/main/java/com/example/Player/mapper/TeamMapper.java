package com.example.Player.mapper;

import com.example.Player.DTO.TeamDTO;
import com.example.Player.models.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeamMapper {
    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    @Mapping(source = "league.code", target = "leagueCode")
    TeamDTO teamToTeamDTO(Team team);
}
