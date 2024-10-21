package com.example.Player.repository;

import com.example.Player.models.FantasyTeam;
import com.example.Player.models.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StadiumRepository extends JpaRepository<Stadium, Long> {
    Optional<Stadium> findByFantasyTeam(FantasyTeam fantasyTeam);
}
