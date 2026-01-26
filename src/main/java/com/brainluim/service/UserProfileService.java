package com.brainluim.service;

import com.brainluim.model.UserProfile;
import com.brainluim.repository.UserProfileRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Avatar colors for visual distinction
    private static final String[] AVATAR_COLORS = {
            "#5b8def", "#f59e0b", "#10b981", "#8b5cf6",
            "#ef4444", "#06b6d4", "#ec4899", "#f97316"
    };

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public UserProfile createProfile(String name, String level, String grade, String pin) {
        String hashedPin = passwordEncoder.encode(pin);
        String avatarColor = getRandomAvatarColor();

        UserProfile profile = new UserProfile(name, level, grade, avatarColor);
        profile.setPinHash(hashedPin);

        return userProfileRepository.save(profile);
    }

    public UserProfile updateProfile(String userId, String name, String level, String grade, String pin) {
        Optional<UserProfile> existing = userProfileRepository.findById(userId);

        if (existing.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        UserProfile profile = existing.get();
        profile.setName(name);
        profile.setLevel(level);
        profile.setGrade(grade);

        if (pin != null && !pin.isEmpty()) {
            profile.setPinHash(passwordEncoder.encode(pin));
        }

        profile.setUpdatedAt(LocalDateTime.now());

        return userProfileRepository.save(profile);
    }

    public UserProfile getProfileById(String userId) {
        return userProfileRepository.findById(userId).orElse(null);
    }

    public List<UserProfile> getAllProfiles() {
        return userProfileRepository.findAll();
    }

    public UserProfile verifyPinAndGetUser(String userId, String pin) {
        UserProfile profile = getProfileById(userId);

        if (profile == null) {
            return null;
        }

        if (passwordEncoder.matches(pin, profile.getPinHash())) {
            return profile;
        }

        return null;
    }

    public boolean hasAnyUsers() {
        return userProfileRepository.count() > 0;
    }

    public void deleteProfile(String userId) {
        userProfileRepository.deleteById(userId);
    }

    private String getRandomAvatarColor() {
        Random random = new Random();
        return AVATAR_COLORS[random.nextInt(AVATAR_COLORS.length)];
    }
}