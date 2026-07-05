package com.drikek.improveMe.refreshToken;

import com.drikek.improveMe.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<@NonNull RefreshToken, @NonNull Long> {

    Optional<RefreshToken> findByToken(String token);

    // Use for "Logout"
    List<RefreshToken> findByUser(User user);

    // Use for cleanup refresh token already used
    List<RefreshToken> findByExpiresDateBefore(LocalDateTime dateTime);
}
