package com.example.lab.dto.license;

public class ActivateLicenseRequest {

    private String licenseKey;
    private String deviceIdentifier;

    public ActivateLicenseRequest() {}

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }
}