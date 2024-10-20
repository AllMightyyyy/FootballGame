package com.example.Player.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScoreDTO {

    @JsonProperty("ht")
    private List<Integer> ht = new ArrayList<>();

    @JsonProperty("ft")
    private List<Integer> ft = new ArrayList<>();

    // Getters and Setters

    public List<Integer> getHt() {
        return ht;
    }

    public void setHt(List<Integer> ht) {
        this.ht = ht;
    }

    public List<Integer> getFt() {
        return ft;
    }

    public void setFt(List<Integer> ft) {
        this.ft = ft;
    }
}
