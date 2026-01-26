package com.brainluim.dto;

import java.time.LocalDateTime;

public class LessonResponse {
    private String id;
    private String content;
    private String subject;
    private String fileName;
    private String level;
    private String grade;
    private LocalDateTime uploadTime;

    public LessonResponse(String id, String content, String subject,
                          String fileName, String level, String grade, LocalDateTime uploadTime) {
        this.id = id;
        this.content = content;
        this.subject = subject;
        this.fileName = fileName;
        this.level = level;
        this.grade = grade;
        this.uploadTime = uploadTime;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }
}