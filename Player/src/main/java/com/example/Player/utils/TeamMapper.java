// src/main/java/com/example/Player/utils/TeamMapper.java

package com.example.Player.utils;

import com.example.Player.models.Team;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeamMapper {
    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    TeamDTO teamToTeamDTO(Team team);
}
