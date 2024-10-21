package com.example.Player.repository;

import com.example.Player.models.Referee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefereeRepository extends JpaRepository<Referee, Long> {
    // Add custom query methods if needed
}
