package com.brainluim.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brainluim.dto.AuthRequest;
import com.brainluim.dto.AuthResponse;
import com.brainluim.dto.UserProfileResponse;
import com.brainluim.dto.UserSummaryResponse;

import com.brainluim.exception.InvalidRequestException;

import com.brainluim.model.UserProfile;

import com.brainluim.service.UserProfileService;

import com.brainluim.util.JwtUtil;

@RestController
@RequestMapping("/api/v1/auth/users")
public class AuthController {

    private final UserProfileService userProfileService;
    private final JwtUtil jwtUtil;

    public AuthController(UserProfileService userProfileService, JwtUtil jwtUtil) {
        this.userProfileService = userProfileService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<List<UserSummaryResponse>> getAll() {
        List<UserProfile> users = userProfileService.getAllProfiles();

        List<UserSummaryResponse> response = users.stream()
                .map(user -> new UserSummaryResponse(
                        user.getId(),
                        user.getName(),
                        user.getAvatarColor()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

        if (request.getUserId() == null || request.getUserId().isEmpty()) {
            throw new InvalidRequestException("User ID is required");
        }

        if (request.getPin() == null || request.getPin().length() != 6) {
            throw new InvalidRequestException("PIN must be 6 digits");
        }

        UserProfile profile = userProfileService.verifyPinAndGetUser(
                request.getUserId(),
                request.getPin()
        );

        if (profile == null) {
            throw new InvalidRequestException("Invalid PIN");
        }

        String token = jwtUtil.generateToken(profile.getId(), profile.getName());

        UserProfileResponse profileResponse = new UserProfileResponse(
                profile.getId(),
                profile.getName(),
                profile.getLevel(),
                profile.getGrade(),
                profile.getAvatarColor()
        );

        AuthResponse response = new AuthResponse(token, profileResponse);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    public ResponseEntity<UserProfileResponse> verifyToken(@RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidRequestException("Invalid authorization header");
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.isTokenValid(token)) {
            throw new InvalidRequestException("Invalid or expired token");
        }

        String userId = jwtUtil.extractUserId(token);
        UserProfile profile = userProfileService.getProfileById(userId);

        if (profile == null) {
            throw new InvalidRequestException("User not found");
        }

        UserProfileResponse response = new UserProfileResponse(
                profile.getId(),
                profile.getName(),
                profile.getLevel(),
                profile.getGrade(),
                profile.getAvatarColor()
        );

        return ResponseEntity.ok(response);
    }
}