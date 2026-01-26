package com.brainluim.dto;

public class AuthResponse {
    private String token;
    private UserProfileResponse profile;

    public AuthResponse(String token, UserProfileResponse profile) {
        this.token = token;
        this.profile = profile;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserProfileResponse getProfile() {
        return profile;
    }

    public void setProfile(UserProfileResponse profile) {
        this.profile = profile;
    }
}