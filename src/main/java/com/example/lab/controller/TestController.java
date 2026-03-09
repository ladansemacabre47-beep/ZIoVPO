package com.example.lab.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/secure/test")
    public String test(Authentication authentication) {
        return "Hello, " + authentication.getName();
    }
}