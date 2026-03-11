package com.example.lab.dto.license;

import java.time.LocalDateTime;

public class LicenseCheckResponse {

    private String licenseKey;
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean blocked;
    private String signature;

    public LicenseCheckResponse() {}

    public LicenseCheckResponse(String licenseKey,
                                LocalDateTime start,
                                LocalDateTime end,
                                boolean blocked,
                                String signature) {
        this.licenseKey = licenseKey;
        this.start = start;
        this.end = end;
        this.blocked = blocked;
        this.signature = signature;
    }

    public String getLicenseKey() { return licenseKey; }
    public void setLicenseKey(String licenseKey) { this.licenseKey = licenseKey; }
    public LocalDateTime getStart() { return start; }
    public void setStart(LocalDateTime start) { this.start = start; }
    public LocalDateTime getEnd() { return end; }
    public void setEnd(LocalDateTime end) { this.end = end; }
    public boolean isBlocked() { return blocked; }
    public void setBlocked(boolean blocked) { this.blocked = blocked; }
    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }
}