package com.brainluim.controller;

import java.io.IOException;

import java.util.List;

import java.util.stream.Collectors;

import com.brainluim.util.JwtUtil;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;

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
    private final JwtUtil jwtUtil;

    /**
     * Constructs a new {@link LessonController}.
     *
     * @param lessonService service responsible for lesson persistence and retrieval
     * @param userProfileService service responsible for fetching user profile data
     * @param jwtUtil utility for JWT token operations
     */
    public LessonController(LessonService lessonService, UserProfileService userProfileService, JwtUtil jwtUtil) {
        this.lessonService = lessonService;
        this.userProfileService = userProfileService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Creates a new lesson by uploading a lesson file and subject.
     *
     * @param file the uploaded lesson file (multipart/form-data)
     * @param subject the subject associated with the lesson
     * @param authHeader the Authorization header containing the JWT token
     * @return a {@link ResponseEntity} containing the lesson creation response
     * @throws IOException if an error occurs while processing the uploaded file
     * @throws InvalidRequestException if the file is missing or empty
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<LessonResponse> create(
            @RequestParam("lessonFile") MultipartFile file,
            @RequestParam("subject") String subject,
            @RequestHeader("Authorization") String authHeader) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new InvalidRequestException("Please upload a valid file.");
        }

        // Extract userId from JWT
        String token = authHeader.substring(7);
        String userId = jwtUtil.extractUserId(token);

        Lesson lesson = lessonService.createLesson(file, subject, userId);

        UserProfile profile = userProfileService.getProfileById(userId);
        String level = profile != null ? profile.getLevel() : "Unknown";
        String grade = profile != null ? profile.getGrade() : "Unknown";

        LessonResponse response = new LessonResponse(
                lesson.getId(),
                lesson.getContent(),
                lesson.getSubject(),
                lesson.getFileName(),
                level,
                grade,
                lesson.getUploadTime()
        );

        return ResponseEntity.status(201).body(response);
    }

    /**
     * Retrieves a lesson by its unique identifier.
     *
     * @param id the lesson ID
     * @param authHeader the Authorization header containing the JWT token
     * @return a {@link ResponseEntity} containing the detailed lesson information
     * @throws InvalidRequestException if the lesson does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<LessonResponse> getById(
            @PathVariable String id,
            @RequestHeader("Authorization") String authHeader) {

        Lesson lesson = lessonService.getLessonById(id);

        if (lesson == null) {
            throw new InvalidRequestException("Lesson not found with id: " + id);
        }

        // Extract userId from JWT
        String token = authHeader.substring(7);
        String userId = jwtUtil.extractUserId(token);

        UserProfile profile = userProfileService.getProfileById(userId);
        String level = profile != null ? profile.getLevel() : "Unknown";
        String grade = profile != null ? profile.getGrade() : "Unknown";

        LessonResponse response = new LessonResponse(
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
     * @param authHeader the Authorization header containing the JWT token
     * @return a {@link ResponseEntity} containing a list of lesson summaries
     */
    @GetMapping
    public ResponseEntity<List<LessonSummaryResponse>> getAll(@RequestHeader("Authorization") String authHeader) {

        // Extract userId from JWT
        String token = authHeader.substring(7);
        String userId = jwtUtil.extractUserId(token);

        List<Lesson> lessons = lessonService.getLessonsByUserId(userId);

        UserProfile profile = userProfileService.getProfileById(userId);
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