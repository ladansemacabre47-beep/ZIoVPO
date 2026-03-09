package com.example.lab.service.license;

import com.example.lab.dto.license.Ticket;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class TicketSignerService {

    @Value("${license.ticket.secret}")
    private String secret;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String sign(Ticket ticket) {
        try {
            String data = objectMapper.writeValueAsString(ticket);

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            mac.init(keySpec);

            byte[] signatureBytes = mac.doFinal(data.getBytes());

            return Base64.getEncoder().encodeToString(signatureBytes);

        } catch (Exception e) {
            throw new RuntimeException("Error signing ticket", e);
        }
    }
}