package com.example.lab.model.license;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class LicenseType {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    private int defaultDurationInDays;

    private String description;

    public LicenseType() {}

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDefaultDurationInDays() {
        return defaultDurationInDays;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDefaultDurationInDays(int defaultDurationInDays) {
        this.defaultDurationInDays = defaultDurationInDays;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}