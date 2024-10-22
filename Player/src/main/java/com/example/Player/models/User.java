// src/main/java/com/example/Player/models/User.java

package com.example.Player.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // Manager Name

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // Hashed password

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Team team;

    // New field for enabled status
    @Column(nullable = false)
    private boolean enabled;

    // Implementing UserDetails methods

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // No roles/authorities for now
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    // Account status flags

    @Override
    public boolean isAccountNonExpired() {
        return true; // Modify as per your requirements
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Modify as per your requirements
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Modify as per your requirements
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
