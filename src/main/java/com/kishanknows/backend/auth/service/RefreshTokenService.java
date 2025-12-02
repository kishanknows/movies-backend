package com.kishanknows.backend.auth.service;

import com.kishanknows.backend.auth.model.RefreshToken;
import com.kishanknows.backend.auth.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository repo;
    private static final long REFRESH_EXPIRATION_MS = 1000L * 60 * 60 * 24 * 7;

    public RefreshTokenService(RefreshTokenRepository repo) {
        this.repo = repo;
    }

    public String createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(REFRESH_EXPIRATION_MS));

        RefreshToken saved = repo.save(refreshToken);
        return saved.getToken();
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().isBefore(Instant.now())){
            repo.delete(token);
            throw new RuntimeException("Refresh token expired");
        }

        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return repo.findByToken(token);
    }

    public void revokeToken(String token){
        repo.deleteByToken(token);
    }
}
