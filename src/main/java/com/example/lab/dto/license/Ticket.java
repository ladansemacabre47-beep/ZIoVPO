package com.example.lab.dto.license;

import java.time.LocalDateTime;

public class Ticket {

    private LocalDateTime serverDate;
    private long ticketLifetimeSeconds;
    private LocalDateTime activationDate;
    private LocalDateTime expirationDate;
    private Long userId;
    private Long deviceId;
    private boolean blocked;

    public Ticket(LocalDateTime serverDate,
                  long ticketLifetimeSeconds,
                  LocalDateTime activationDate,
                  LocalDateTime expirationDate,
                  Long userId,
                  Long deviceId,
                  boolean blocked) {
        this.serverDate = serverDate;
        this.ticketLifetimeSeconds = ticketLifetimeSeconds;
        this.activationDate = activationDate;
        this.expirationDate = expirationDate;
        this.userId = userId;
        this.deviceId = deviceId;
        this.blocked = blocked;
    }

    public LocalDateTime getServerDate() {
        return serverDate;
    }

    public long getTicketLifetimeSeconds() {
        return ticketLifetimeSeconds;
    }

    public LocalDateTime getActivationDate() {
        return activationDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public boolean isBlocked() {
        return blocked;
    }
}