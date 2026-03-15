package com.example.lab.config;

import com.example.lab.service.CustomUserDetailsService;
import com.example.lab.security.JwtTokenProvider;
import com.example.lab.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider tokenProvider;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          JwtTokenProvider tokenProvider) {
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        JwtAuthenticationFilter jwtFilter =
                new JwtAuthenticationFilter(tokenProvider, userDetailsService);

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        // Signatures — чтение доступно всем авторизованным
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/signatures").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/signatures/increment").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/signatures/by-ids").hasAnyRole("ADMIN", "USER")

                        // Signatures — запись только ADMIN
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/signatures").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/signatures/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/signatures/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/signatures/*/history").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/signatures/*/audit").hasRole("ADMIN")

                        // Licenses
                        .requestMatchers("/licenses/create").hasRole("ADMIN")
                        .requestMatchers("/licenses/activate").hasRole("USER")
                        .requestMatchers("/licenses/check").hasRole("USER")
                        .requestMatchers("/licenses/renew").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}