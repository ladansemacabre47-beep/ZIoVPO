package com.example.lab.controller;

import com.example.lab.dto.license.Ticket;
import com.example.lab.service.license.TicketSignerService;
import com.example.lab.service.license.TicketVerifierService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    private final TicketSignerService signerService;
    private final TicketVerifierService verifierService;

    public TestController(TicketSignerService signerService,
                          TicketVerifierService verifierService) {
        this.signerService = signerService;
        this.verifierService = verifierService;
    }

    @GetMapping("/signature/deterministic")
    public Map<String, Object> testDeterministic() {
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0, 0);

        Ticket ticket1 = new Ticket(now, 3600L, now, now.plusHours(1), 1L, 1L, false);
        Ticket ticket2 = new Ticket(now, 3600L, now, now.plusHours(1), 1L, 1L, false);

        String sig1 = signerService.sign(ticket1);
        String sig2 = signerService.sign(ticket2);

        return Map.of(
                "signature1", sig1,
                "signature2", sig2,
                "deterministic", sig1.equals(sig2)
        );
    }

    @GetMapping("/signature")
    public Map<String, Object> testSignature() {
        Ticket ticket = new Ticket(
                LocalDateTime.now(),
                3600L,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                1L,
                1L,
                false
        );


        String signature = signerService.sign(ticket);
        boolean valid = verifierService.verify(ticket, signature);

        return Map.of(
                "signature", signature,
                "valid", valid
        );
    }
}