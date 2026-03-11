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
    private final TicketSignerService ticketSignerService;
    private final JsonCanonicalizationService canonicalizationService;
    private final SignatureKeyProvider keyProvider;

    public LicenseService(
            LicenseRepository licenseRepository,
            DeviceRepository deviceRepository,
            UserRepository userRepository,
            TicketSignerService ticketSignerService,
            JsonCanonicalizationService canonicalizationService,
            SignatureKeyProvider keyProvider
    ) {
        this.licenseRepository = licenseRepository;
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
        this.ticketSignerService = ticketSignerService;
        this.canonicalizationService = canonicalizationService;
        this.keyProvider = keyProvider;
    }

    private String signPayload(Object payload) {
        try {
            byte[] canonicalBytes = canonicalizationService.canonicalize(payload);
            java.security.Signature sig = java.security.Signature.getInstance("SHA256withRSA");
            sig.initSign(keyProvider.getPrivateKey());
            sig.update(canonicalBytes);
            return java.util.Base64.getEncoder().encodeToString(sig.sign());
        } catch (Exception e) {
            throw new RuntimeException("Error signing payload", e);
        }
    }

    public License create(CreateLicenseRequest request) {
        License license = new License();
        license.setLicenseKey(UUID.randomUUID().toString());
        license.setBlocked(false);
        return licenseRepository.save(license);
    }

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

        Ticket ticket = new Ticket(
                LocalDateTime.now(),
                TICKET_LIFETIME_SECONDS,
                license.getFirstActivationDate(),
                license.getEndingDate(),
                user.getId(),
                device.getId(),
                license.isBlocked()
        );

        String signature = ticketSignerService.sign(ticket);

        return new TicketResponse(
                ticket.getServerDate(),
                ticket.getTicketLifetimeSeconds(),
                ticket.getActivationDate(),
                ticket.getExpirationDate(),
                ticket.getUserId(),
                ticket.getDeviceId(),
                ticket.isBlocked(),
                signature
        );
    }

    public LicenseCheckResponse check(LicenseCheckRequest request) {
        License license = licenseRepository
                .findByLicenseKey(request.getLicenseKey())
                .orElseThrow(() -> new RuntimeException("License not found"));

        LicenseCheckResponse response = new LicenseCheckResponse(
                license.getLicenseKey(),
                license.getFirstActivationDate(),
                license.getEndingDate(),
                license.isBlocked(),
                null
        );

        String signature = signPayload(response);
        response.setSignature(signature);

        return response;
    }

    public LicenseResponse renew(RenewLicenseRequest request) {
        License license = licenseRepository
                .findByLicenseKey(request.getLicenseKey())
                .orElseThrow(() -> new RuntimeException("License not found"));

        license.setEndingDate(
                license.getEndingDate().plusDays(request.getDays())
        );

        licenseRepository.save(license);

        LicenseResponse response = new LicenseResponse(
                license.getId(),
                license.getLicenseKey(),
                license.getFirstActivationDate(),
                license.getEndingDate(),
                license.isBlocked(),
                null
        );

        String signature = signPayload(response);
        response = new LicenseResponse(
                license.getId(),
                license.getLicenseKey(),
                license.getFirstActivationDate(),
                license.getEndingDate(),
                license.isBlocked(),
                signature
        );

        return response;
    }
}