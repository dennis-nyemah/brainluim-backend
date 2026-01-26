package com.brainluim.service;

import com.brainluim.repository.LessonRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.brainluim.model.Lesson;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class LessonService {

    private LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public Lesson createLesson(MultipartFile file, String subject, String userId) throws IOException {

        String content = "";
        String fileName = "Unknown";

        if (file != null && file.getSize() > 0 && !file.isEmpty()) {
            fileName = file.getOriginalFilename();

            String contentType = file.getContentType();
            if (contentType == null) {
                throw new IllegalArgumentException("Unable to determine file type");
            }

            if (contentType.startsWith("text/")) {
                content = new String(file.getBytes(), StandardCharsets.UTF_8);
            } else if (contentType.equals("application/pdf")) {
                content = extractTextFromPDF(file);
            } else {
                throw new IllegalArgumentException("Unsupported file type: " + contentType);
            }

            content = cleanAndValidateText(content);
        }

        if (content.trim().isEmpty()) {
            throw new IllegalArgumentException("No readable content found in the uploaded file");
        }

        if (content.length() < 50) {
            throw new IllegalArgumentException("Content is too short. Please provide more substantial lesson material.");
        }

        Lesson lesson = new Lesson(content, subject, fileName, userId);

        return lessonRepository.save(lesson);
    }

    public Lesson getLessonById(String id) {
        Optional<Lesson> lesson = lessonRepository.findById(id);
        return lesson.orElse(null);
    }

    public List<Lesson> getLessonsByUserId(String userId) {
        return lessonRepository.findByUserId(userId);
    }

    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    private String extractTextFromPDF(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException e) {
            throw new IOException("Failed to extract text from PDF: " + e.getMessage(), e);
        }
    }

    private String cleanAndValidateText(String text) {
        if (text == null) return "";

        return text.trim()
                .replaceAll("\\r\\n", "\n")
                .replaceAll("\\r", "\n")
                .replaceAll("\\n{3,}", "\n\n")
                .replaceAll("[ \\t]+", " ");
    }
}