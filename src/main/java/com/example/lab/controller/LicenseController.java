package com.example.lab.controller;

import com.example.lab.dto.license.*;
import com.example.lab.model.license.License;
import com.example.lab.service.license.LicenseService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/licenses")
public class LicenseController {

    private final LicenseService licenseService;

    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @PostMapping("/create")
    public LicenseResponse create(@RequestBody CreateLicenseRequest request) {
        License license = licenseService.create(request);
        return new LicenseResponse(
                license.getId(),
                license.getLicenseKey(),
                license.getFirstActivationDate(),
                license.getEndingDate(),
                license.isBlocked(),
                null
        );
    }

    @PostMapping("/activate")
    public TicketResponse activate(
            @RequestBody ActivateLicenseRequest request,
            Authentication authentication
    ) {
        return licenseService.activate(request, authentication.getName());
    }

    @PostMapping("/check")
    public LicenseCheckResponse check(@RequestBody LicenseCheckRequest request) {
        return licenseService.check(request);
    }

    @PostMapping("/renew")
    public LicenseResponse renew(@RequestBody RenewLicenseRequest request) {
        return licenseService.renew(request);
    }
}