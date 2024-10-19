// src/main/java/com/example/Player/utils/PlayerLeagueResponseDTO.java

package com.example.Player.utils;

public class PlayerLeagueResponseDTO {
    private String code;
    private String name;
    private String season;

    public PlayerLeagueResponseDTO(String code, String name, String season) {
        this.code = code;
        this.name = name;
        this.season = season;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }
}
