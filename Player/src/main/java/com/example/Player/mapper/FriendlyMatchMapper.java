package com.example.Player.mapper;

import com.example.Player.DTO.FantasyTeamDTO;
import com.example.Player.DTO.FriendlyMatchDTO;
import com.example.Player.models.FantasyTeam;
import com.example.Player.models.FriendlyMatch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FriendlyMatchMapper {
    FriendlyMatchMapper INSTANCE = Mappers.getMapper(FriendlyMatchMapper.class);

    // Correctly map the properties based on your FriendlyMatch entity
    @Mapping(source = "team1", target = "requesterTeam")
    @Mapping(source = "team2", target = "targetTeam")
    FriendlyMatchDTO toDTO(FriendlyMatch match);

    // Helper method for mapping FantasyTeam to FantasyTeamDTO
    default FantasyTeamDTO mapFantasyTeam(FantasyTeam fantasyTeam) {
        return new FantasyTeamDTO(
                fantasyTeam.getId(),
                fantasyTeam.getTeamName(),
                fantasyTeam.getBalance(),
                null, // Handle TeamDTO as needed
                null, // Handle UserDTO as needed
                null // Handle FantasyLeagueDTO as needed
        );
    }
}
