package com.example.Player.controllers;

import com.example.Player.models.Player;
import com.example.Player.services.FormationService;
import com.example.Player.services.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayerController.class)
class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private FormationService formationService;

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player();
        player.setId(1L);
        player.setShortName("Lionel Messi");
    }

    @Test
    void searchPlayers_ReturnsPlayers() throws Exception {
        when(playerService.searchPlayers(anyString(), anyList(), anyList(), anyList(), anyList(),
                anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(),
                anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(player)));

        mockMvc.perform(get("/api/players")
                        .param("page", "1")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.players[0].shortName").value("Lionel Messi"));
    }

    @Test
    void getPlayerById_ReturnsPlayer_WhenPlayerExists() throws Exception {
        when(playerService.getPlayerById(1L)).thenReturn(player);

        mockMvc.perform(get("/api/players/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortName").value("Lionel Messi"));
    }

    @Test
    void addPlayerToFormation_ReturnsSuccess_WhenPlayerIsAdded() throws Exception {
        when(playerService.getPlayerById(anyLong())).thenReturn(player);
        when(formationService.addPlayerToPosition(anyString(), any(Player.class))).thenReturn(true);

        mockMvc.perform(post("/api/players/formation")
                        .param("position", "ST")
                        .param("playerId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Player added successfully"));
    }
}
