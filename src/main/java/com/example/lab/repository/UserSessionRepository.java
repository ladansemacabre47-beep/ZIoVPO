package com.example.lab.repository;

import com.example.lab.entity.UserSession;
import com.example.lab.entity.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findByRefreshToken(String refreshToken);
    List<UserSession> findByUserIdAndStatus(Long userId, SessionStatus status);
}
