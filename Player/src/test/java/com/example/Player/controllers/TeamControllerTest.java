package com.example.Player.controllers;

import com.example.Player.models.League;
import com.example.Player.models.Team;
import com.example.Player.models.User;
import com.example.Player.services.LeagueService;
import com.example.Player.services.TeamService;
import com.example.Player.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeamController.class)
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamService teamService;

    @MockBean
    private LeagueService leagueService;

    @MockBean
    private UserService userService;

    private League league;

    @BeforeEach
    void setUp() {
        league = new League();
        league.setCode("epl");
        league.setName("English Premier League");
    }

    @Test
    void getLeagues_ReturnsLeagues() throws Exception {
        when(leagueService.getAllLeagues()).thenReturn(Collections.singletonList(league));

        mockMvc.perform(get("/api/teams/all-leagues"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("epl"))
                .andExpect(jsonPath("$[0].name").value("English Premier League"));
    }

    @Test
    @WithMockUser
    void getUserTeam_ReturnsUserTeam_WhenAuthenticated() throws Exception {
        User user = new User();
        Team team = new Team();
        team.setId(1L);
        team.setName("Team A");
        team.setLeague(league);

        when(userService.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(teamService.getUserTeam(user)).thenReturn(Optional.of(team));

        mockMvc.perform(get("/api/teams/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Team A"))
                .andExpect(jsonPath("$.leagueCode").value("epl"));
    }
}
