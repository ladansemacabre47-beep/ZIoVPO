package com.example.lab.model.license;

import com.example.lab.model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class LicenseHistory {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private License license;

    @ManyToOne
    private User user;

    private String status;

    private LocalDateTime changeDate;

    private String description;

    public LicenseHistory() {}

    public UUID getId() { return id; }

    public License getLicense() { return license; }

    public User getUser() { return user; }

    public String getStatus() { return status; }

    public LocalDateTime getChangeDate() { return changeDate; }

    public String getDescription() { return description; }

    public void setLicense(License license) { this.license = license; }

    public void setUser(User user) { this.user = user; }

    public void setStatus(String status) { this.status = status; }

    public void setChangeDate(LocalDateTime changeDate) { this.changeDate = changeDate; }

    public void setDescription(String description) { this.description = description; }
}