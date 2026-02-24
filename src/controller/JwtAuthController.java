package controller;

import com.example.cinema.dto.LoginRequest;
import com.example.cinema.dto.RefreshRequest;
import com.example.cinema.dto.TokenResponse;
import com.example.cinema.entity.UserEntity;
import com.example.cinema.entity.UserSession;
import com.example.cinema.repository.UserRepository;
import com.example.cinema.security.JwtTokenProvider;
import com.example.cinema.service.UserSessionService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class JwtAuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final UserSessionService sessionService;

    public JwtAuthController(AuthenticationManager authenticationManager,
                             UserRepository userRepository,
                             JwtTokenProvider tokenProvider,
                             UserSessionService sessionService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.sessionService = sessionService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req, HttpServletRequest httpReq) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
            );
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        UserEntity user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // create sessionId (string)
        String sessionId = "sess-" + java.util.UUID.randomUUID().toString();

        // generate tokens
        String access = tokenProvider.generateAccessToken(user.getUsername(), user.getRole(), user.getId(), sessionId);
        String refresh = tokenProvider.generateRefreshToken(user.getUsername(), user.getId(), sessionId);

        Instant refreshExpiry = Instant.now().plusMillis(tokenProviderRefreshMs());

        // save session
        String ip = httpReq.getRemoteAddr();
        String ua = httpReq.getHeader("User-Agent");
        sessionService.createSession(user, refresh, refreshExpiry, ip, ua);

        TokenResponse resp = new TokenResponse(access, refresh, tokenProviderAccessMs()/1000);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest req, HttpServletRequest httpReq) {
        String refreshToken = req.getRefreshToken();
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest().body("refreshToken required");
        }

        if (!tokenProvider.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(401).body("Invalid refresh token");
        }

        Optional<UserSession> sessOpt = sessionService.findByRefreshToken(refreshToken);
        if (sessOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Session not found");
        }
        UserSession session = sessOpt.get();

        if (session.getStatus() != com.example.cinema.entity.SessionStatus.ACTIVE) {
            return ResponseEntity.status(403).body("Session not active");
        }

        if (session.getExpiresAt().isBefore(Instant.now())) {
            sessionService.revokeSession(session);
            return ResponseEntity.status(403).body("Session expired");
        }

        // parse refresh token, read username + sessionId
        Jws<Claims> claims = tokenProvider.parseRefreshToken(refreshToken);
        String username = claims.getBody().getSubject();
        String sessionId = claims.getBody().get("sessionId", String.class);

        // rotate: generate new refresh token, new access
        String newSessionId = "sess-" + java.util.UUID.randomUUID().toString();
        String newRefresh = tokenProvider.generateRefreshToken(username, session.getUser().getId(), newSessionId);
        String newAccess = tokenProvider.generateAccessToken(username, session.getUser().getRole(), session.getUser().getId(), newSessionId);

        Instant newExpiry = Instant.now().plusMillis(tokenProviderRefreshMs());

        // mark old as REPLACED and create new session
        UserSession newSess = sessionService.rotateSession(session, newRefresh, newExpiry);

        TokenResponse resp = new TokenResponse(newAccess, newRefresh, tokenProviderAccessMs()/1000);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshRequest req) {
        String refreshToken = req.getRefreshToken();
        if (refreshToken == null || refreshToken.isBlank()) return ResponseEntity.ok("ok");

        Optional<UserSession> sessOpt = sessionService.findByRefreshToken(refreshToken);
        if (sessOpt.isEmpty()) return ResponseEntity.ok("ok");

        sessionService.markLoggedOut(sessOpt.get());
        return ResponseEntity.ok("logged out");
    }

    // helpers to read provider settings (avoid exposing provider internals)
    private long tokenProviderAccessMs() {
        // reflection not needed — read property via provider if you prefer;
        // we just use default via parsing application.properties again:
        try {
            java.lang.reflect.Field f = tokenProvider.getClass().getDeclaredField("accessExpirationMs");
            f.setAccessible(true);
            return (long) f.get(tokenProvider);
        } catch (Exception e) {
            return 900000L;
        }
    }

    private long tokenProviderRefreshMs() {
        try {
            java.lang.reflect.Field f = tokenProvider.getClass().getDeclaredField("refreshExpirationMs");
            f.setAccessible(true);
            return (long) f.get(tokenProvider);
        } catch (Exception e) {
            return 604800000L;
        }
    }
}
