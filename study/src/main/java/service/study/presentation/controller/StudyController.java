package service.study.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.study.business.usecase.student.StudyUseCase;
import service.study.presentation.mapping.GuideMapper;
import service.study.presentation.response.GuideResponse;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/study/students")
@RequiredArgsConstructor
@Slf4j
public class StudyController {
    
    private final StudyUseCase studyUseCase;
    private final GuideMapper guideMapper;
    
    /**
     * API: Học sinh tham gia lesson
     * POST /api/study/students/{studentId}/lessons/{lessonId}/join
     */
    @PostMapping("/{studentId}/lessons/{lessonId}/join")
    public ResponseEntity<Map<String, Object>> joinLesson(
            @PathVariable Long studentId,
            @PathVariable Long lessonId) {
        log.info("Student {} joining lesson {}", studentId, lessonId);
        
        boolean success = studyUseCase.joinLesson(studentId, lessonId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? "Successfully joined lesson" : "Failed to join lesson");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * API: Lấy tiến độ học tập của học sinh trong một khóa học
     * GET /api/study/students/{studentId}/courses/{courseId}/progress
     */
    @GetMapping("/{studentId}/courses/{courseId}/progress")
    public ResponseEntity<Map<String, Object>> getLearningProgress(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        log.info("Getting learning progress for student {} in course {}", studentId, courseId);
        
        Double progress = studyUseCase.getLearningProgress(studentId, courseId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("studentId", studentId);
        response.put("courseId", courseId);
        response.put("progress", progress);
        response.put("unit", "%");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * API: Lấy các gợi ý tương ứng với level của học sinh ở mỗi lesson
     * GET /api/study/students/{studentId}/lessons/{lessonId}/guides?level=...
     */
    @GetMapping("/{studentId}/lessons/{lessonId}/guides")
    public ResponseEntity<GuideResponse> getGuidesByLesson(
            @PathVariable Long studentId,
            @PathVariable Long lessonId,
            @RequestParam(required = false) String level) {
        log.info("Getting guides for student {} in lesson {} with level {}", studentId, lessonId, level);
        
        var guides = studyUseCase.getGuidesByLesson(studentId, lessonId, level);
        var guideDTOs = guideMapper.toDTOList(guides);
        
        GuideResponse response = GuideResponse.builder()
                .guides(guideDTOs)
                .total(guideDTOs.size())
                .build();
        
        return ResponseEntity.ok(response);
    }
}