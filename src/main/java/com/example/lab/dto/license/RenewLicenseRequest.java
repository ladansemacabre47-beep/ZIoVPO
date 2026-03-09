package com.example.lab.dto.license;

public class RenewLicenseRequest {

    private String licenseKey;
    private int days;

    public RenewLicenseRequest(){}

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}