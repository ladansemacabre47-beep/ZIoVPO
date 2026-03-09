package com.example.lab.model.license;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "licenses")
public class License {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String licenseKey;

    private LocalDateTime firstActivationDate;

    private LocalDateTime endingDate;

    private boolean blocked;

    public License() {
    }

    public Long getId() {
        return id;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public LocalDateTime getFirstActivationDate() {
        return firstActivationDate;
    }

    public void setFirstActivationDate(LocalDateTime firstActivationDate) {
        this.firstActivationDate = firstActivationDate;
    }

    public LocalDateTime getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(LocalDateTime endingDate) {
        this.endingDate = endingDate;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}