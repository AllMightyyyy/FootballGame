// src/main/java/com/example/Player/repository/LeagueRepository.java

package com.example.Player.repository;

import com.example.Player.models.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {
    Optional<League> findByName(String name);
    Optional<League> findByCode(String code);
    boolean existsByCode(String code);
    Optional<League> findByCodeAndSeason(String code, String season);
    boolean existsByName(String name);
    Optional<League> findTopByCodeOrderBySeasonDesc(String code);

    // Add this method for partial name matching
    @Query("SELECT l FROM League l WHERE l.name LIKE CONCAT(:name, '%')")
    List<League> findByNameStartingWith(@Param("name") String name);
}
