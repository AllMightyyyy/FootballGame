package com.example.Player.controllers;

import com.example.Player.models.League;
import com.example.Player.services.FootballDataService;
import com.example.Player.services.StandingService;
import com.example.Player.DTO.LeagueDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FootballDataController.class)
class FootballDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FootballDataService footballDataService;

    @MockBean
    private StandingService standingService;

    private League league;

    @BeforeEach
    void setUp() {
        league = new League();
        league.setCode("epl");
        league.setName("English Premier League");
        league.setSeason("2024/25");
    }

    @Test
    void getLeagueData_ReturnsLeagueData_WhenLeagueExists() throws Exception {
        LeagueDTO leagueDTO = new LeagueDTO();
        leagueDTO.setMatches(Collections.emptyList());
        leagueDTO.setStandings(Collections.emptyList());

        when(footballDataService.getLeagueDataByCode(anyString())).thenReturn(league);
        when(standingService.calculateStandings(Mockito.any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/football/league/epl"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("epl"))
                .andExpect(jsonPath("$.name").value("English Premier League"))
                .andExpect(jsonPath("$.season").value("2024/25"))
                .andExpect(jsonPath("$.matches").isArray())
                .andExpect(jsonPath("$.standings").isArray());
    }

    @Test
    void getLeagueData_ReturnsNotFound_WhenLeagueDoesNotExist() throws Exception {
        when(footballDataService.getLeagueDataByCode(anyString())).thenReturn(null);

        mockMvc.perform(get("/api/football/league/unknown"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("League not found with code: unknown"));
    }

    @Test
    void getTeams_ReturnsTeams_WhenLeagueExists() throws Exception {
        when(footballDataService.getLeagueByCode(anyString())).thenReturn(Optional.of(league));

        mockMvc.perform(get("/api/football/teams/epl"))
                .andExpect(status().isOk());
    }

    @Test
    void getStandings_ReturnsStandings_WhenLeagueExists() throws Exception {
        LeagueDTO leagueDTO = new LeagueDTO();
        when(footballDataService.getStandingsDTOByCode(anyString())).thenReturn(leagueDTO);

        mockMvc.perform(get("/api/football/standings/epl"))
                .andExpect(status().isOk());
    }
}
