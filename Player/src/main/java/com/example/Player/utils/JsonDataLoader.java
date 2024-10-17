package com.example.Player.utils;

import com.example.Player.models.League;
import com.example.Player.models.Match;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class JsonDataLoader {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static League loadLeagueData(String leagueFile) throws IOException {
        ClassPathResource resource = new ClassPathResource("JsonData/2024-25/" + leagueFile);
        InputStream inputStream = resource.getInputStream();
        League league = objectMapper.readValue(inputStream, League.class);
        inputStream.close();
        System.out.println(league); // Use the variable instead of reading the stream again
        return league;
    }


    /* Test if works
    public static void main(String[] args) {
        try {
            League league = loadLeagueData("en.1.json");
            for (Match match : league.getMatches()) {
                System.out.println(match.getTeam1() + " - " + match.getTeam2());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
        */
}
