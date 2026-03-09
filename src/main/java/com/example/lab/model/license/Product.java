package com.example.lab.model.license;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Product {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    private boolean blocked = false;

    public Product() {}

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}