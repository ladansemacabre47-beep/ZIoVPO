package com.example.lab.controller;

import com.example.lab.dto.UserDTO;
import com.example.lab.entity.UserEntity;
import com.example.lab.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO dto) {


        if (userRepository.existsByUsername(dto.getUsername())) {
            return ResponseEntity.badRequest()
                    .body("Пользователь уже существует");
        }

        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("USER");

        userRepository.save(user);

        return ResponseEntity.ok("Пользователь зарегистрирован");
    }
}