package com.example.Player.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "matches", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"league_id", "date", "time", "team1_id", "team2_id"})
})
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String round;
    private String date; // Consider using LocalDate
    private String time; // Consider using LocalTime or LocalDateTime

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team1_id")
    private Team team1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team2_id")
    private Team team2;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "htTeam1", column = @Column(name = "ht_team1")),
            @AttributeOverride(name = "htTeam2", column = @Column(name = "ht_team2")),
            @AttributeOverride(name = "ftTeam1", column = @Column(name = "ft_team1")),
            @AttributeOverride(name = "ftTeam2", column = @Column(name = "ft_team2"))
    })
    private Score score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id")
    private League league;

    // **Add the fantasyLeague field**
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fantasy_league_id")
    private FantasyLeague fantasyLeague;

    private String status;

    public Match() {
    }

    public Match(String round, String date, String time, Team team1, Team team2, Score score, League league) {
        this.round = round;
        this.date = date;
        this.time = time;
        this.team1 = team1;
        this.team2 = team2;
        this.score = score;
        this.league = league;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FantasyLeague getFantasyLeague() {
        return fantasyLeague;
    }

    public void setFantasyLeague(FantasyLeague fantasyLeague) {
        this.fantasyLeague = fantasyLeague;
    }
}
