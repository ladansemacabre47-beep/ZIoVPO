package com.example.lab.service;

import com.example.lab.entity.UserEntity;
import com.example.lab.entity.UserSession;
import com.example.lab.entity.SessionStatus;
import com.example.lab.repository.UserSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
public class UserSessionService {

    private final UserSessionRepository repository;

    public UserSessionService(UserSessionRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UserSession createSession(UserEntity user, String refreshToken, Instant expiresAt, String ip, String ua) {
        UserSession s = new UserSession();
        s.setUser(user);
        s.setRefreshToken(refreshToken);
        s.setStatus(SessionStatus.ACTIVE);
        s.setCreatedAt(Instant.now());
        s.setExpiresAt(expiresAt);
        s.setIpAddress(ip);
        s.setUserAgent(ua);
        return repository.save(s);
    }

    @Transactional
    public UserSession rotateSession(UserSession oldSession, String newRefreshToken, Instant newExpiry) {
        // mark old as REPLACED
        oldSession.setStatus(SessionStatus.REPLACED);
        repository.save(oldSession);

        // create new session with same user, ip/ua copied
        UserSession s = new UserSession();
        s.setUser(oldSession.getUser());
        s.setRefreshToken(newRefreshToken);
        s.setStatus(SessionStatus.ACTIVE);
        s.setCreatedAt(Instant.now());
        s.setExpiresAt(newExpiry);
        s.setIpAddress(oldSession.getIpAddress());
        s.setUserAgent(oldSession.getUserAgent());
        return repository.save(s);
    }

    @Transactional
    public void revokeSession(UserSession s) {
        s.setStatus(SessionStatus.REVOKED);
        repository.save(s);
    }

    @Transactional
    public void markLoggedOut(UserSession s) {
        s.setStatus(SessionStatus.LOGGED_OUT);
        repository.save(s);
    }

    public Optional<UserSession> findByRefreshToken(String refreshToken) {
        return repository.findByRefreshToken(refreshToken);
    }

    public Optional<UserSession> findById(Long id) {
        return repository.findById(id);
    }
}
