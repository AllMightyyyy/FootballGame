// src/main/java/com/example/Player/DataLoader.java
package com.example.Player.utils;

import com.example.Player.models.User;
import com.example.Player.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        String defaultUsername = "devuser";
        String defaultEmail = "devuser@example.com";
        String defaultPassword = "devpassword";

        if (userService.isUsernameAvailable(defaultUsername)) {
            userService.registerUser(defaultUsername, defaultEmail, defaultPassword);
            System.out.println("Default user 'devuser' created.");
        } else {
            System.out.println("Default user 'devuser' already exists.");
        }
    }
}
