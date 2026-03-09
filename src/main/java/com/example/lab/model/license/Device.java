package com.example.lab.model.license;

import com.example.lab.model.User;
import jakarta.persistence.*;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceIdentifier;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    public Device() {}

    public Long getId() {
        return id;
    }

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public User getOwner() {
        return owner;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}