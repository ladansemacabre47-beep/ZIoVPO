package com.example.lab.dto.license;

public class LicenseCheckRequest {

    private String licenseKey;

    public LicenseCheckRequest(){}

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }
}