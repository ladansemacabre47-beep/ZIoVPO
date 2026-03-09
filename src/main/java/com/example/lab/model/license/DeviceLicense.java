package com.example.lab.model.license;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class DeviceLicense {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private License license;

    @ManyToOne(optional = false)
    private Device device;

    private LocalDateTime activationDate;

    public DeviceLicense() {}

    public UUID getId() { return id; }

    public License getLicense() { return license; }

    public Device getDevice() { return device; }

    public LocalDateTime getActivationDate() { return activationDate; }

    public void setLicense(License license) { this.license = license; }

    public void setDevice(Device device) { this.device = device; }

    public void setActivationDate(LocalDateTime activationDate) { this.activationDate = activationDate; }
}