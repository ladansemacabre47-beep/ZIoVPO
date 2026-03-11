package com.example.lab.dto.license;

import java.time.LocalDateTime;

public class LicenseResponse {

    private Long id;
    private String licenseKey;
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean blocked;
    private String signature;

    public LicenseResponse() {}

    public LicenseResponse(Long id,
                           String licenseKey,
                           LocalDateTime start,
                           LocalDateTime end,
                           boolean blocked,
                           String signature) {
        this.id = id;
        this.licenseKey = licenseKey;
        this.start = start;
        this.end = end;
        this.blocked = blocked;
        this.signature = signature;
    }

    public Long getId() { return id; }
    public String getLicenseKey() { return licenseKey; }
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }
    public boolean isBlocked() { return blocked; }
    public String getSignature() { return signature; }
}