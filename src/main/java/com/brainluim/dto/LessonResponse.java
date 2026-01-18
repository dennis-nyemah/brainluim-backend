package com.brainluim.dto;

public class LessonResponse {
    private String id;
    private String message;

    public LessonResponse(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}

