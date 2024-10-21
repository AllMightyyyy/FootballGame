// src/main/java/com/example/Player/repository/TeamRepository.java

package com.example.Player.repository;

import com.example.Player.models.League;
import com.example.Player.models.Team;
import com.example.Player.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByNameAndLeague(String name, League league);
    boolean existsByNameAndLeague(String name, League league);
    List<Team> findAllByLeague(League league);
    boolean existsByName(String name);
    Optional<Team> findByUser(User user);
    Optional<Team> findByName(String name);
}
