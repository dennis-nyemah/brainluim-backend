package com.brainluim.dto;

public class UserProfileRequest {
    private String name;
    private String level;
    private String grade;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}