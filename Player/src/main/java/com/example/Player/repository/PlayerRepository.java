// src/main/java/com/example/Player/repository/PlayerRepository.java
package com.example.Player.repository;

import com.example.Player.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long>, JpaSpecificationExecutor<Player> {
    // Additional query methods can be defined here if needed
}
