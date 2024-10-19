package com.example.Player;

import com.example.Player.models.League;
import com.example.Player.services.LeagueService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.Player.models")
public class PlayerApplication {

	private static LeagueService leagueService;

	public static void main(String[] args) {
		SpringApplication.run(PlayerApplication.class, args);
			League league = new League("Test League", "test", "2024-25");
			leagueService.saveLeague(league);
	}

}
