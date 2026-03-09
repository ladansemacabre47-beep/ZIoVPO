package com.example.lab.service.license;

import com.example.lab.dto.license.Ticket;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyStore;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

@Service
public class TicketVerifierService {

    @Value("${license.keystore.path}")
    private String keystorePath;

    @Value("${license.keystore.password}")
    private String keystorePassword;

    @Value("${license.key.alias}")
    private String keyAlias;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public boolean verify(Ticket ticket, String signatureBase64) {
        try {
            String data = objectMapper.writeValueAsString(ticket);

            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(
                    getClass().getClassLoader().getResourceAsStream(keystorePath),
                    keystorePassword.toCharArray()
            );

            PublicKey publicKey = keyStore.getCertificate(keyAlias).getPublicKey();

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(data.getBytes());

            return signature.verify(Base64.getDecoder().decode(signatureBase64));

        } catch (Exception e) {
            throw new RuntimeException("Error verifying ticket signature", e);
        }
    }
}