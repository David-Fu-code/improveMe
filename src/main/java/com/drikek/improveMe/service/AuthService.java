package com.drikek.improveMe.service;

import com.drikek.improveMe.dto.AuthResponse;
import com.drikek.improveMe.dto.LoginRequest;
import com.drikek.improveMe.dto.LogoutAllResponse;
import com.drikek.improveMe.dto.RegisteredRequest;
import com.drikek.improveMe.email.EmailSender;
import com.drikek.improveMe.entity.AppUserRole;
import com.drikek.improveMe.entity.User;
import com.drikek.improveMe.exception.AuthException;
import com.drikek.improveMe.refreshToken.RefreshToken;
import com.drikek.improveMe.refreshToken.RefreshTokenRepository;
import com.drikek.improveMe.refreshToken.RefreshTokenService;
import com.drikek.improveMe.repository.UserRepository;
import com.drikek.improveMe.service.token.ConfirmationTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class AuthService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    // REGISTER
    public User registerUser(RegisteredRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AuthException("email already registered", 409);
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .appUserRole(AppUserRole.USER)
                .displayName(request.getDisplayName())
                .verified(false)
                .locked(false)
                .enabled(false)
                .build();

        userService.saveUser(user);

        String token = confirmationTokenService.createTokenForUser(user);

        String link = "http://localhost:8080/api/v1/auth/confirm?token=" + token;
        emailSender.send(request.getEmail(),buildEmail(request.getEmail(), link) );

        return userRepository.save(user);
    }

    // Create Email
    private String buildEmail(String name, String link) {
        return "<p>Hello " + name + ",</p>"
                + "<p>Thank you for registering. Please click on the below link to activate your account:</p>"
                + "<a href=\"" + link + "\">Confirm Account</a>"
                + "<p>The link will expire in 15 minutes.</p>"
                + "<p>See you soon!</p>";
    }

    // LOGIN
    // always creates a new token
    @Transactional
    public AuthResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("user name not found with email: " + request.getEmail(), 404));

        if (!user.isVerified() || !user.isEnabled()) {
            throw new AuthException("Please confirm your email or account is disable", 403);

        } else if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException("Invalid credential, please try again", 401);
        }

        // If user login creates access and refresh Token
        String accessToken = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getAppUserRole().name())
                .build();
    }

    // CONFIRM
    @Transactional
    public String confirmToken(String token) {
        ConfirmationTokenService.ConfirmationResult result = confirmationTokenService.setConfirmedAt(token);

        if (result.firstTime()) {
            User user = result.user();
            user.setVerified(true);
            user.setEnabled(true);
            userService.saveUser(user);
        }
        return result.message();
    }

    public User getCurrentUser() {
        // Get authentication object from SecurityContext
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()){
            throw new AuthException("No authenticated user found", 404);
        }
        // Extract Jwt token from authentication
        String token = (String) authentication.getCredentials();
        if (token == null){
            throw new AuthException("No jwt token found", 404);
        }

        // Extract email from JWT
        String email = jwtService.extractEmail(token);

        // Load user from the db
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("User not found", 404));
    }

    // REFRESH TOKENS
    // Creates new refreshToken and mark old as used
    public AuthResponse refreshToken(String oldToken) {

        // Validate the refresh token and get the user
        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(oldToken);
        User user = refreshToken.getUser();

        // rotate: old token is now invalid forever
        refreshTokenService.markRefreshTokenAsUsed(refreshToken);

        // Create new access + refresh
        String newAccess = jwtService.generateToken(user);
        String newRefresh = refreshTokenService.createRefreshToken(user);

        // Return response
        return AuthResponse.builder()
                .accessToken(newAccess)
                .refreshToken(newRefresh)
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getAppUserRole().name())
                .build();
    }

    // LOGOUT
    // Invalidate only the current token
    public void logoutSingleToken(String refreshToken) {
        refreshTokenService.validateRefreshToken(refreshToken);
        refreshTokenService.markRefreshTokenAsUsed(refreshToken);
    }

    // When user Logout invalidate all tokens for all devices
    @Transactional
    public LogoutAllResponse logoutAllUserTokens(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("User not found with email: " + email, 404));

        List<RefreshToken> tokens = refreshTokenRepository.findByUser(user);
        for (RefreshToken token : tokens) {
            token.setUsed(true);
        }
        refreshTokenRepository.saveAll(tokens);

        return new LogoutAllResponse(
                "All sessions logged out successfully",
                LocalDateTime.now()
        );
    }



}
