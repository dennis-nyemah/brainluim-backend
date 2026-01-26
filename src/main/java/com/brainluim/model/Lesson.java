package com.brainluim.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    private String id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String subject;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "user_id", nullable = false)  // IMPORTANT: Not null
    private String userId;

    @Column(name = "created_at")
    private LocalDateTime uploadTime = LocalDateTime.now();

    protected Lesson() {}

    public Lesson(String content, String subject, String fileName, String userId) {
        this.id = UUID.randomUUID().toString();
        this.content = content;
        this.subject = subject;
        this.fileName = fileName;
        this.userId = userId;
    }

    // Getters and Setters
    public String getId() { return id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public LocalDateTime getUploadTime() { return uploadTime; }
    public void setUploadTime(LocalDateTime uploadTime) { this.uploadTime = uploadTime; }
}