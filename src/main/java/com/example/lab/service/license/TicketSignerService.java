package com.example.lab.service.license;

import com.example.lab.dto.license.Ticket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Signature;
import java.util.Base64;

@Service
public class TicketSignerService {

    @Value("${license.signature.algorithm:SHA256withRSA}")
    private String signatureAlgorithm;

    private final SignatureKeyProvider keyProvider;
    private final JsonCanonicalizationService canonicalizationService;

    public TicketSignerService(SignatureKeyProvider keyProvider,
                               JsonCanonicalizationService canonicalizationService) {
        this.keyProvider = keyProvider;
        this.canonicalizationService = canonicalizationService;
    }

    public String sign(Ticket ticket) {
        try {
            byte[] canonicalBytes = canonicalizationService.canonicalize(ticket);

            Signature signature = Signature.getInstance(signatureAlgorithm);
            signature.initSign(keyProvider.getPrivateKey());
            signature.update(canonicalBytes);

            return Base64.getEncoder().encodeToString(signature.sign());

        } catch (Exception e) {
            throw new RuntimeException("Error signing ticket", e);
        }
    }
}