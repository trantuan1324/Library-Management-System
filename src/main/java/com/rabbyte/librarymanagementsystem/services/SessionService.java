package com.rabbyte.librarymanagementsystem.services;

import com.rabbyte.librarymanagementsystem.entities.Session;
import com.rabbyte.librarymanagementsystem.entities.User;
import com.rabbyte.librarymanagementsystem.repositories.SessionRepository;
import com.rabbyte.librarymanagementsystem.security.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class SessionService {

    @Value("${jwt.refresh-token.expiration-time}")
    private long refreshTokenTTL;

    private final SessionRepository sessionRepository;
    private final JwtUtils jwtUtils;

    @Transactional
    public Session createSession(User user) {
        // Revoke tất cả refresh token cũ của user
        sessionRepository.revokeAllByUserId(user.getId());
        Instant expiresAtInstant = Instant.now().plusMillis(refreshTokenTTL);

        Session newSession = new Session();
        newSession.setUser(user);
        newSession.setRefreshToken(jwtUtils.generateRefreshToken());
        newSession.setExpiresAt(LocalDateTime.ofInstant(expiresAtInstant, ZoneId.of("UTC")));
        newSession.setRevoked(false);

        return sessionRepository.save(newSession);
    }

    @Transactional
    public Session verifyExpiration(Session session) {
        if (session.isExpired() || session.isRevoked()) {
            sessionRepository.delete(session);
            throw new RuntimeException("Session has expired or revoked.");
        }

        return session;
    }

    @Transactional
    public void deleteByToken(String token) {
        sessionRepository.deleteByRefreshToken(token);
    }

    public Session findByToken(String token) {
        return sessionRepository.findByRefreshToken(token).orElseThrow(() -> new RuntimeException("Session not found."));
    }

    @Transactional
    public void revokeAllTokensByUser(Long userId) {
        sessionRepository.deleteAllByUserId(userId);
    }
}
