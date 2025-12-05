package com.drikek.improveMe.controller;

import com.drikek.improveMe.dto.*;
import com.drikek.improveMe.entity.User;
import com.drikek.improveMe.exception.AuthException;
import com.drikek.improveMe.repository.UserRepository;
import com.drikek.improveMe.service.AuthService;
import com.drikek.improveMe.service.token.ConfirmationTokenService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;


    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody RegisteredRequest request) {
            User user = authService.registerUser(request); // Throws AuthException if email exists

            UserResponse response = UserResponse.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .displayName(user.getDisplayName())
                    .appUserRole(user.getAppUserRole())
                    .build();

            return ResponseEntity.ok(response);
    }

    @GetMapping("/confirm")
    public ResponseEntity<@NonNull String> confirm(@RequestParam("token") String token) {
        String result = authService.confirmToken(token); // Throw AuthException if invalid
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody LoginRequest request) {
            AuthResponse tokens = authService.login(request); // Throw AuthException if credentials invalid
            return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<@NonNull AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest request) {
        authService.logoutSingleToken(request.getRefreshToken());
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/logout-all")
    public ResponseEntity<?> logoutAll(@RequestBody LogoutAllRequest request) {
        // Obtain user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("User not found with email: " + request.getEmail(), 404));

        String message = authService.logoutAllUserTokens(user);

        LogoutAllResponse response = new LogoutAllResponse(
                message,
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);
    }



}
