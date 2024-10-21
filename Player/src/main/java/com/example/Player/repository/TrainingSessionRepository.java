package com.example.Player.repository;

import com.example.Player.models.FantasyPlayer;
import com.example.Player.models.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
    List<TrainingSession> findByEndTimeBefore(LocalDateTime time);

    boolean existsByFantasyPlayerAndEndTimeAfter(FantasyPlayer player, LocalDateTime time);
}
