package com.drikek.improveMe.controller;

import com.drikek.improveMe.dto.*;
import com.drikek.improveMe.entity.User;
import com.drikek.improveMe.repository.UserRepository;
import com.drikek.improveMe.service.AuthService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@SuppressWarnings("unused")
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
        LogoutAllResponse response = authService.logoutAllUserTokens(request.getEmail());
        return ResponseEntity.ok(response);
    }



}
