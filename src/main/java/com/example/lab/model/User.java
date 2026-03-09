package com.example.lab.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;
    private String password;
    private String role;
    private boolean blocked = false;

    public User() {}

    public Long getId() { return id; }

    public String getEmail() { return email; }

    public String getName() { return name; }

    public String getPassword() { return password; }

    public String getRole() { return role; }

    public boolean isBlocked() { return blocked; }

    public void setEmail(String email) { this.email = email; }

    public void setName(String name) { this.name = name; }

    public void setPassword(String password) { this.password = password; }

    public void setRole(String role) { this.role = role; }

    public void setBlocked(boolean blocked) { this.blocked = blocked; }
}