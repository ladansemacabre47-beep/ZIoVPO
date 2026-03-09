package com.example.lab.service.license;

import com.example.lab.dto.license.*;
import com.example.lab.entity.UserEntity;
import com.example.lab.model.license.Device;
import com.example.lab.model.license.License;
import com.example.lab.repository.UserRepository;
import com.example.lab.repository.license.DeviceRepository;
import com.example.lab.repository.license.LicenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LicenseService {

    private static final long TICKET_LIFETIME_SECONDS = 300;

    private final LicenseRepository licenseRepository;
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    public LicenseService(
            LicenseRepository licenseRepository,
            DeviceRepository deviceRepository,
            UserRepository userRepository
    ) {
        this.licenseRepository = licenseRepository;
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
    }

    // создание лицензии
    public License create(CreateLicenseRequest request) {

        License license = new License();

        license.setLicenseKey(UUID.randomUUID().toString());
        license.setBlocked(false);

        return licenseRepository.save(license);
    }

    // активация лицензии
    public TicketResponse activate(ActivateLicenseRequest request, String username) {

        UserEntity user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        License license = licenseRepository
                .findByLicenseKey(request.getLicenseKey())
                .orElseThrow(() -> new RuntimeException("License not found"));

        Device device = deviceRepository
                .findByDeviceIdentifier(request.getDeviceIdentifier())
                .orElseGet(() -> {

                    Device d = new Device();
                    d.setDeviceIdentifier(request.getDeviceIdentifier());

                    return deviceRepository.save(d);
                });

        if (license.getFirstActivationDate() == null) {
            license.setFirstActivationDate(LocalDateTime.now());
        }

        if (license.getEndingDate() == null) {
            license.setEndingDate(LocalDateTime.now().plusDays(30));
        }

        licenseRepository.save(license);

        return new TicketResponse(
                LocalDateTime.now(),
                TICKET_LIFETIME_SECONDS,
                license.getFirstActivationDate(),
                license.getEndingDate(),
                user.getId(),
                device.getId(),
                license.isBlocked()
        );
    }

    // проверка лицензии
    public License check(LicenseCheckRequest request) {

        return licenseRepository
                .findByLicenseKey(request.getLicenseKey())
                .orElseThrow(() -> new RuntimeException("License not found"));
    }

    // продление лицензии
    public License renew(RenewLicenseRequest request) {

        License license = licenseRepository
                .findByLicenseKey(request.getLicenseKey())
                .orElseThrow(() -> new RuntimeException("License not found"));

        license.setEndingDate(
                license.getEndingDate().plusDays(request.getDays())
        );

        return licenseRepository.save(license);
    }
}