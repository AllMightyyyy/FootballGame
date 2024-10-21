package com.example.Player.services;

import com.example.Player.models.FantasyTeam;
import com.example.Player.models.Sponsor;
import com.example.Player.repository.SponsorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SponsorService {

    @Autowired
    private SponsorRepository sponsorRepository;

    @Autowired
    private FantasyTeamService fantasyTeamService;

    public Sponsor assignSponsor(FantasyTeam fantasyTeam, String sponsorName, double contribution, String direction) throws Exception {
        List<Sponsor> currentSponsors = sponsorRepository.findByFantasyTeam(fantasyTeam);
        if (currentSponsors.size() >= 4) {
            throw new Exception("Maximum number of sponsors reached.");
        }

        Sponsor sponsor = new Sponsor();
        sponsor.setSponsorName(sponsorName);
        sponsor.setContribution(contribution);
        sponsor.setFantasyTeam(fantasyTeam);
        sponsor.setDirection(direction);
        sponsorRepository.save(sponsor);

        // Update team's balance based on sponsor's contribution
        fantasyTeam.setBalance(fantasyTeam.getBalance() + contribution);
        fantasyTeamService.saveFantasyTeam(fantasyTeam);

        return sponsor;
    }

    // Additional methods like removing sponsors can be added here
}
