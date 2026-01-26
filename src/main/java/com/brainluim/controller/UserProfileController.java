package com.brainluim.controller;

import com.brainluim.dto.UserProfileRequest;
import com.brainluim.dto.UserProfileResponse;
import com.brainluim.exception.InvalidRequestException;
import com.brainluim.model.UserProfile;
import com.brainluim.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profiles")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping
    public ResponseEntity<UserProfileResponse> create(@RequestBody UserProfileRequest request) {

        if (request.getPin() == null || !request.getPin().matches("\\d{6}")) {
            throw new InvalidRequestException("PIN must be exactly 6 digits");
        }

        UserProfile profile = userProfileService.createProfile(
                request.getName(),
                request.getLevel(),
                request.getGrade(),
                request.getPin()
        );

        UserProfileResponse response = new UserProfileResponse(
                profile.getId(),
                profile.getName(),
                profile.getLevel(),
                profile.getGrade(),
                profile.getAvatarColor()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getById(@PathVariable String id) {
        UserProfile profile = userProfileService.getProfileById(id);

        if (profile == null) {
            return ResponseEntity.notFound().build();
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