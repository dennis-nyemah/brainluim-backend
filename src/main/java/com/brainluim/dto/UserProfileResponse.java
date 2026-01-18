package com.brainluim.dto;

public class UserProfileResponse {
    private String id;
    private String name;
    private String level;
    private String grade;

    public UserProfileResponse(String id, String name, String level, String grade) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.grade = grade;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}