package com.brainluim.controller;

import com.brainluim.dto.LessonDetailResponse;
import com.brainluim.dto.LessonResponse;
import com.brainluim.dto.LessonSummaryResponse;
import com.brainluim.exception.InvalidRequestException;
import com.brainluim.model.Lesson;
import com.brainluim.model.UserProfile;
import com.brainluim.service.LessonService;
import com.brainluim.service.UserProfileService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/lessons")
public class LessonController {

    private LessonService lessonService;
    private final UserProfileService userProfileService;

    public LessonController(LessonService lessonService, UserProfileService userProfileService) {
        this.lessonService = lessonService;
        this.userProfileService = userProfileService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<LessonResponse> create(
            @RequestParam("lessonFile") MultipartFile file,
            @RequestParam("subject") String subject) throws IOException {

        if (file == null || file.getSize() == 0 || file.isEmpty()) {
            throw new InvalidRequestException("Please upload a file.");
        }

        Lesson lesson = lessonService.createLesson(file, subject);

        LessonResponse response = new LessonResponse(
                lesson.getId(),
                "Lesson created successfully"
        );

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDetailResponse> getLesson(@PathVariable String id) {
        Lesson lesson = lessonService.getLessonById(id);

        if (lesson == null) {
            throw new InvalidRequestException("Lesson not found with id: " + id);
        }

        // Get user profile for level and grade
        UserProfile profile = userProfileService.getProfile();
        String level = profile != null ? profile.getLevel() : "Unknown";
        String grade = profile != null ? profile.getGrade() : "Unknown";

        LessonDetailResponse response = new LessonDetailResponse(
                lesson.getId(),
                lesson.getContent(),
                lesson.getSubject(),
                lesson.getFileName(),
                level,
                grade,
                lesson.getUploadTime()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<LessonSummaryResponse>> getAllLessons() {
        List<Lesson> lessons = lessonService.getAllLessons();

        // Get user profile for level and grade
        UserProfile profile = userProfileService.getProfile();
        String level = profile != null ? profile.getLevel() : "Unknown";
        String grade = profile != null ? profile.getGrade() : "Unknown";

        List<LessonSummaryResponse> response = lessons.stream()
                .map(lesson -> new LessonSummaryResponse(
                        lesson.getId(),
                        lesson.getSubject(),
                        lesson.getFileName(),
                        level,
                        grade,
                        lesson.getUploadTime()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}