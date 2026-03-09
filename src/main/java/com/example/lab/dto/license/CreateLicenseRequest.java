package com.example.lab.dto.license;

public class CreateLicenseRequest {

    private int lifetimeDays;

    public int getLifetimeDays() {
        return lifetimeDays;
    }

    public void setLifetimeDays(int lifetimeDays) {
        this.lifetimeDays = lifetimeDays;
    }
}