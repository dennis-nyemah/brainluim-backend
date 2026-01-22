package com.brainluim.controller;

import java.io.IOException;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;

import com.brainluim.dto.LessonDetailResponse;
import com.brainluim.dto.LessonResponse;
import com.brainluim.dto.LessonSummaryResponse;

import com.brainluim.exception.InvalidRequestException;

import com.brainluim.model.Lesson;
import com.brainluim.model.UserProfile;

import com.brainluim.service.LessonService;
import com.brainluim.service.UserProfileService;

/**
 * REST controller responsible for managing lesson-related operations.
 *
 * <p>This controller exposes endpoints for:
 * <ul>
 *   <li>Uploading and creating lessons</li>
 *   <li>Fetching a single lesson by ID</li>
 *   <li>Retrieving all available lessons</li>
 * </ul>
 *
 * <p>All endpoints are prefixed with: <b>/api/v1/lessons</b>
 */
@RestController
@RequestMapping("/api/v1/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final UserProfileService userProfileService;

    /**
     * Constructs a new {@link LessonController}.
     *
     * @param lessonService service responsible for lesson persistence and retrieval
     * @param userProfileService service responsible for fetching user profile data
     */
    public LessonController(LessonService lessonService, UserProfileService userProfileService) {
        this.lessonService = lessonService;
        this.userProfileService = userProfileService;
    }

    /**
     * Creates a new lesson by uploading a lesson file and subject.
     *
     * @param file the uploaded lesson file (multipart/form-data)
     * @param subject the subject associated with the lesson
     * @return a {@link ResponseEntity} containing the lesson creation response
     * @throws IOException if an error occurs while processing the uploaded file
     * @throws InvalidRequestException if the file is missing or empty
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<LessonResponse> create(
            @RequestParam("lessonFile") MultipartFile file,
            @RequestParam("subject") String subject) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new InvalidRequestException("Please upload a valid file.");
        }

        Lesson lesson = lessonService.createLesson(file, subject);

        LessonResponse response = new LessonResponse(
                lesson.getId(),
                "Lesson created successfully"
        );

        return ResponseEntity.status(201).body(response);
    }

    /**
     * Retrieves a lesson by its unique identifier.
     *
     * @param id the lesson ID
     * @return a {@link ResponseEntity} containing the detailed lesson information
     * @throws InvalidRequestException if the lesson does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<LessonDetailResponse> getLesson(@PathVariable String id) {
        Lesson lesson = lessonService.getLessonById(id);

        if (lesson == null) {
            throw new InvalidRequestException("Lesson not found with id: " + id);
        }

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

    /**
     * Retrieves all available lessons.
     *
     * @return a {@link ResponseEntity} containing a list of lesson summaries
     */
    @GetMapping("/all")
    public ResponseEntity<List<LessonSummaryResponse>> getAllLessons() {
        List<Lesson> lessons = lessonService.getAllLessons();

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
