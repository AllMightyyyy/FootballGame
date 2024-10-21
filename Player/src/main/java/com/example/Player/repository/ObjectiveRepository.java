package com.example.Player.repository;

import com.example.Player.models.FantasyTeam;
import com.example.Player.models.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectiveRepository extends JpaRepository<Objective, Long> {
    List<Objective> findByFantasyTeam(FantasyTeam fantasyTeam);
}
