package com.drikek.improveMe.refreshToken;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefreshTokenCleanupService {

    private final RefreshTokenRepository refreshTokenRepository;

    // Executes every day at midnight
    // Eliminate all refresh tokens already used
    @Scheduled(cron = "0 0 0 * * *")
    public void removeExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();

        List<RefreshToken> expiredTokens = refreshTokenRepository.findByExpiresDateBefore(now);

        if (!expiredTokens.isEmpty()) {
            refreshTokenRepository.deleteAll(expiredTokens);
            System.out.println("Deleted " + expiredTokens.size() + " expired refresh tokens.");
        }
    }

}
