package com.brainluim.dto;

public class UserSummaryResponse {
    private String id;
    private String name;
    private String avatarColor;

    public UserSummaryResponse(String id, String name, String avatarColor) {
        this.id = id;
        this.name = name;
        this.avatarColor = avatarColor;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAvatarColor() { return avatarColor; }
    public void setAvatarColor(String avatarColor) { this.avatarColor = avatarColor; }
}