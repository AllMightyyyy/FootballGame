// RefereeService.java
package com.example.Player.services;

import com.example.Player.models.Referee;
import com.example.Player.models.RefereeLeniency;
import com.example.Player.repository.RefereeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class RefereeService {

    @Autowired
    private RefereeRepository refereeRepository;

    private Random random = new Random();

    /**
     * Assigns a referee to a match based on availability and leniency.
     */
    public Referee assignRefereeToMatch() throws Exception {
        List<Referee> referees = refereeRepository.findAll();
        if (referees.isEmpty()) {
            throw new Exception("No referees available.");
        }

        // Simple random assignment; can be enhanced based on availability and scheduling
        return referees.get(random.nextInt(referees.size()));
    }
}
