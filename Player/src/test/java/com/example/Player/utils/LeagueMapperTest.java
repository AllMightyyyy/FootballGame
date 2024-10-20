// src/test/java/com/example/Player/utils/LeagueMapperTest.java

package com.example.Player.utils;

import com.example.Player.DTO.LeagueDTO;
import com.example.Player.DTO.MatchDTO;
import com.example.Player.DTO.StandingDTO;
import com.example.Player.models.League;
import com.example.Player.models.Match;
import com.example.Player.models.Score;
import com.example.Player.models.Team;
import org.testng.annotations.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class LeagueMapperTest {

    @Test
    public void testLeagueToLeagueDTO() {
        // Setup
        League league = new League();
        league.setName("English Premier League");
        league.setCode("en.1");
        league.setSeason("2024-25");

        Team team1 = new Team();
        team1.setName("Manchester United");

        Team team2 = new Team();
        team2.setName("Liverpool");

        Match match1 = new Match();
        match1.setRound("1");
        match1.setDate("2024-08-10");
        match1.setTime("15:00");
        match1.setTeam1(team1);
        match1.setTeam2(team2);
        match1.setScore(new Score(1, 0, 2, 1));
        match1.setLeague(league);

        league.setMatches(Arrays.asList(match1));

        // Action
        LeagueDTO leagueDTO = LeagueMapper.INSTANCE.leagueToLeagueDTO(league);
        leagueDTO.setStandings(Arrays.asList(new StandingDTO("Manchester United"), new StandingDTO("Liverpool")));

        // Assertions
        assertEquals("English Premier League", leagueDTO.getName());
        assertEquals("en.1", leagueDTO.getCode());
        assertEquals("2024-25", leagueDTO.getSeason());
        assertEquals(1, leagueDTO.getMatches().size());

        MatchDTO matchDTO = leagueDTO.getMatches().get(0);
        assertEquals("1", matchDTO.getRound());
        assertEquals("2024-08-10", matchDTO.getDate());
        assertEquals("15:00", matchDTO.getTime());
        assertEquals("Manchester United", matchDTO.getTeam1());
        assertEquals("Liverpool", matchDTO.getTeam2());
        assertNotNull(matchDTO.getScore());
        assertEquals(Arrays.asList(1, 0), matchDTO.getScore().getHt());
        assertEquals(Arrays.asList(2, 1), matchDTO.getScore().getFt());

        assertEquals(2, leagueDTO.getStandings().size());
    }
}
