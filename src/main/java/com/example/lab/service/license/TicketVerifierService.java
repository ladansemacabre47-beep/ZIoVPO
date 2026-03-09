package com.example.lab.service.license;

import com.example.lab.dto.license.Ticket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Signature;
import java.util.Base64;

@Service
public class TicketVerifierService {

    @Value("${license.signature.algorithm:SHA256withRSA}")
    private String signatureAlgorithm;

    private final SignatureKeyProvider keyProvider;
    private final JsonCanonicalizationService canonicalizationService;

    public TicketVerifierService(SignatureKeyProvider keyProvider,
                                 JsonCanonicalizationService canonicalizationService) {
        this.keyProvider = keyProvider;
        this.canonicalizationService = canonicalizationService;
    }

    public boolean verify(Ticket ticket, String signatureBase64) {
        try {
            byte[] canonicalBytes = canonicalizationService.canonicalize(ticket);

            Signature signature = Signature.getInstance(signatureAlgorithm);
            signature.initVerify(keyProvider.getPublicKey());
            signature.update(canonicalBytes);

            return signature.verify(Base64.getDecoder().decode(signatureBase64));

        } catch (Exception e) {
            throw new RuntimeException("Error verifying ticket signature", e);
        }
    }
}