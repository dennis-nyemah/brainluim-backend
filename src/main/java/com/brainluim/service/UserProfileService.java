package com.brainluim.service;

import com.brainluim.model.UserProfile;
import com.brainluim.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private static final String DEFAULT_USER_ID = "default-user";

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfile createOrUpdateProfile(String name, String level, String grade) {
        Optional<UserProfile> existing = userProfileRepository.findById(DEFAULT_USER_ID);

        if (existing.isPresent()) {
            UserProfile profile = existing.get();
            profile.setName(name);
            profile.setLevel(level);
            profile.setGrade(grade);
            profile.setUpdatedAt(LocalDateTime.now());
            return userProfileRepository.save(profile);
        } else {
            UserProfile profile = new UserProfile(name, level, grade);
            return userProfileRepository.save(profile);
        }
    }

    public UserProfile getProfile() {
        return userProfileRepository.findById(DEFAULT_USER_ID).orElse(null);
    }

    public boolean profileExists() {
        return userProfileRepository.existsById(DEFAULT_USER_ID);
    }
}