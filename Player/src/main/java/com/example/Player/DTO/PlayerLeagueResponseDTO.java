// src/main/java/com/example/Player/utils/PlayerLeagueResponseDTO.java

package com.example.Player.DTO;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerLeagueResponseDTO that = (PlayerLeagueResponseDTO) o;
        return code.equals(that.code) && season.equals(that.season);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, season);
    }
}
