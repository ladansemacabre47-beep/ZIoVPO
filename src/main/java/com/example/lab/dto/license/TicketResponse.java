package com.example.lab.dto.license;

import java.time.LocalDateTime;

public class TicketResponse {

    private LocalDateTime serverTime;
    private long lifetime;
    private LocalDateTime licenseStart;
    private LocalDateTime licenseEnd;
    private Long userId;
    private Long deviceId;
    private boolean blocked;
    private String signature;

    public TicketResponse(
            LocalDateTime serverTime,
            long lifetime,
            LocalDateTime licenseStart,
            LocalDateTime licenseEnd,
            Long userId,
            Long deviceId,
            boolean blocked,
            String signature
    ) {
        this.serverTime = serverTime;
        this.lifetime = lifetime;
        this.licenseStart = licenseStart;
        this.licenseEnd = licenseEnd;
        this.userId = userId;
        this.deviceId = deviceId;
        this.blocked = blocked;
        this.signature = signature;
    }

    public LocalDateTime getServerTime() { return serverTime; }
    public long getLifetime() { return lifetime; }
    public LocalDateTime getLicenseStart() { return licenseStart; }
    public LocalDateTime getLicenseEnd() { return licenseEnd; }
    public Long getUserId() { return userId; }
    public Long getDeviceId() { return deviceId; }
    public boolean isBlocked() { return blocked; }
    public String getSignature() { return signature; }
}