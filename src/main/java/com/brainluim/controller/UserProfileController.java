package com.brainluim.controller;

import com.brainluim.dto.UserProfileRequest;
import com.brainluim.dto.UserProfileResponse;
import com.brainluim.model.UserProfile;
import com.brainluim.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping
    public ResponseEntity<UserProfileResponse> createOrUpdateProfile(@RequestBody UserProfileRequest request) {
        UserProfile profile = userProfileService.createOrUpdateProfile(
                request.getName(),
                request.getLevel(),
                request.getGrade()
        );

        UserProfileResponse response = new UserProfileResponse(
                profile.getId(),
                profile.getName(),
                profile.getLevel(),
                profile.getGrade()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<UserProfileResponse> getProfile() {
        UserProfile profile = userProfileService.getProfile();

        if (profile == null) {
            return ResponseEntity.notFound().build();
        }

        UserProfileResponse response = new UserProfileResponse(
                profile.getId(),
                profile.getName(),
                profile.getLevel(),
                profile.getGrade()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> profileExists() {
        return ResponseEntity.ok(userProfileService.profileExists());
    }
}