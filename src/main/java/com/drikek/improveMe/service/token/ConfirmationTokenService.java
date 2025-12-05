package com.drikek.improveMe.service.token;

import com.drikek.improveMe.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken (String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public String createTokenForUser(User user) {
        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(user).build();

        saveConfirmationToken(confirmationToken);

        return token;
    }

    public ConfirmationResult setConfirmedAt(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Invalid or expired confirmation token"));

        String message;
        boolean firstTime = false;

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email already confirmed");
        } else if (confirmationToken.getExpiredAt().isBefore(LocalDateTime.now())){
            throw new IllegalStateException("Invalid or expired confirmation token");
        } else {
            confirmationToken.setConfirmedAt(LocalDateTime.now());
            confirmationTokenRepository.save(confirmationToken);
            message = "Email confirmed successfully";
            firstTime = true;
        }

        return new ConfirmationResult(message, confirmationToken.getUser(), firstTime);
    }

    public static record ConfirmationResult(String message, User user, boolean firstTime){}
}
