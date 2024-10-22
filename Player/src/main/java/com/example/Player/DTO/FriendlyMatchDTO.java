package com.example.Player.DTO;

public class FriendlyMatchDTO {
    private Long id;
    private String status;
    private FantasyTeamDTO requesterTeam;
    private FantasyTeamDTO targetTeam;

    // Constructors, Getters, and Setters

    public FriendlyMatchDTO() {}

    public FriendlyMatchDTO(Long id, String status, FantasyTeamDTO requesterTeam, FantasyTeamDTO targetTeam) {
        this.id = id;
        this.status = status;
        this.requesterTeam = requesterTeam;
        this.targetTeam = targetTeam;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FantasyTeamDTO getRequesterTeam() {
        return requesterTeam;
    }

    public void setRequesterTeam(FantasyTeamDTO requesterTeam) {
        this.requesterTeam = requesterTeam;
    }

    public FantasyTeamDTO getTargetTeam() {
        return targetTeam;
    }

    public void setTargetTeam(FantasyTeamDTO targetTeam) {
        this.targetTeam = targetTeam;
    }
}
