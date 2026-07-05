package com.drikek.improveMe.refreshToken;

import com.drikek.improveMe.entity.User;
import com.drikek.improveMe.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    // Create new refresh token
    public String createRefreshToken(User user) {

        RefreshToken token = new RefreshToken();
        token.setToken(generateRefreshTokenString());
        token.setUser(user);
        token.setExpiresDate(LocalDateTime.now().plusDays(7));
        token.setUsed(false);

        refreshTokenRepository.save(token);
        return token.getToken();
    }

    // Help to create a new access token
    public User getUserFromRefreshToken(String token) {
        RefreshToken refreshToken = validateRefreshToken(token);
        return refreshToken.getUser();
    }

    // Check is token is used or expired
    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new AuthException("refresh token not found", 404));

        if (refreshToken.isUsed()) {
           throw new AuthException("Refresh token already used", 403);

        } else if (refreshToken.getExpiresDate().isBefore(LocalDateTime.now())) {
            throw new AuthException("refresh token is expired", 401);
        }

        return refreshToken;
    }

    // Make sure that refresh token can only be used once
    // Refresh token in memory
    public void markRefreshTokenAsUsed(RefreshToken token) {
        token.setUsed(true);
        refreshTokenRepository.save(token);
    }

    // Frontend send String token
    public void markRefreshTokenAsUsed(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                        .orElseThrow(() -> new AuthException("Refresh token not found", 404));
        refreshToken.setUsed(true);
        refreshTokenRepository.save(refreshToken);
    }

    // new Refresh Token
    public String generateRefreshTokenString() {
        return UUID.randomUUID().toString();
    }
}
