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

    // создание лицензии
    @PostMapping("/create")
    public LicenseResponse create(@RequestBody CreateLicenseRequest request) {

        License license = licenseService.create(request);

        return new LicenseResponse(
                license.getId(),
                license.getLicenseKey(),
                license.getFirstActivationDate(),
                license.getEndingDate(),
                license.isBlocked()
        );
    }

    // активация лицензии
    @PostMapping("/activate")
    public TicketResponse activate(
            @RequestBody ActivateLicenseRequest request,
            Authentication authentication
    ) {

        String username = authentication.getName();

        return licenseService.activate(request, username);
    }

    // проверка лицензии
    @PostMapping("/check")
    public LicenseCheckResponse check(@RequestBody LicenseCheckRequest request) {

        License license = licenseService.check(request);

        return new LicenseCheckResponse(
                license.getLicenseKey(),
                license.getFirstActivationDate(),
                license.getEndingDate(),
                license.isBlocked()
        );
    }

    // продление лицензии
    @PostMapping("/renew")
    public LicenseResponse renew(@RequestBody RenewLicenseRequest request) {

        License license = licenseService.renew(request);

        return new LicenseResponse(
                license.getId(),
                license.getLicenseKey(),
                license.getFirstActivationDate(),
                license.getEndingDate(),
                license.isBlocked()
        );
    }
}