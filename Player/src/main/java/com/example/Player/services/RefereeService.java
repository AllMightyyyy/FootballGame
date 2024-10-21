package com.example.Player.services;

import com.example.Player.models.Referee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefereeService {

    @Autowired
    private RefereeRepository refereeRepository;

    public Referee getRefereeById(Long id) throws Exception {
        return refereeRepository.findById(id)
                .orElseThrow(() -> new Exception("Referee not found."));
    }

    // Additional methods like assigning referees can be added here
}
