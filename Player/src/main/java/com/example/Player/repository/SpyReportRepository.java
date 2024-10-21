package com.example.Player.repository;

import com.example.Player.models.FantasyTeam;
import com.example.Player.models.SpyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpyReportRepository extends JpaRepository<SpyReport, Long> {
    List<SpyReport> findByReportingTeam(FantasyTeam reportingTeam);
}
