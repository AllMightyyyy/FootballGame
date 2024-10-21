package com.example.Player.repository;

import com.example.Player.models.FantasyTeam;
import com.example.Player.models.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Long> {
    List<Sponsor> findByFantasyTeam(FantasyTeam fantasyTeam);
}
