package service.study.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.study.business.usecase.student.ScheduleUseCase;
import service.study.presentation.dto.CourseDTO;
import service.study.presentation.dto.EnrollCourseRequest;
import service.study.presentation.response.CourseListResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/study/schedules")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {
    
    private final ScheduleUseCase scheduleUseCase;

    /**
     * API: Lấy danh sách các khóa học có sẵn cho học sinh (với filter)
     * GET /api/study/schedules/courses?search=...&category=...&minPrice=...&maxPrice=...&level=...&instructorId=...&createdTime=...&page=...&size=...
     */
    @GetMapping("/courses")
    public ResponseEntity<CourseListResponse> getAvailableCourses(
            @RequestParam(defaultValue = "0") Long instructorId,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") String category,
            @RequestParam(defaultValue = "0.0") Double minPrice,
            @RequestParam(defaultValue = "0.0") Double maxPrice,
            @RequestParam(defaultValue = "") String level,
            @RequestParam(defaultValue = "") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("Getting available courses with filters - search: {}, category: {}, minPrice: {}, maxPrice: {}, level: {}, instructorId: {}", 
                search, category, minPrice, maxPrice, level, instructorId);
        
        Pageable pageable = PageRequest.of(page, size);
        List<CourseDTO> courses = scheduleUseCase.getAvailableCourses(
                search, category, minPrice, maxPrice, level, instructorId, createdTime, pageable);
        
        CourseListResponse response = CourseListResponse.builder()
                .courses(courses)
                .total(courses.size())
                .build();
        
        return ResponseEntity.ok(response);
    }

    /**
     * API: Học sinh đăng ký tham gia khóa học
     * POST /api/study/schedules/{studentId}/enroll
     */
    @PostMapping("/{studentId}/enroll")
    public ResponseEntity<Map<String, Object>> enrollCourse(
            @PathVariable Long studentId,
            @RequestBody EnrollCourseRequest request) {
        log.info("Enrolling student {} to course {}", studentId, request.getCourseId());
        
        boolean success = scheduleUseCase.enrollCourse(studentId, request.getCourseId());
        
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("message", "Successfully enrolled in course");
            response.put("success", true);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Already enrolled in this course");
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    /**
     * API: Lấy danh sách khóa học đã đăng ký của học sinh
     * GET /api/study/schedules/{studentId}/courses
     */
    @GetMapping("/{studentId}/courses")
    public ResponseEntity<CourseListResponse> getEnrolledCourses(@PathVariable Long studentId) {
        log.info("Getting enrolled courses for student {}", studentId);
        
        List<CourseDTO> courses = scheduleUseCase.getEnrolledCourses(studentId);
        
        CourseListResponse response = CourseListResponse.builder()
                .courses(courses)
                .total(courses.size())
                .build();
        
        return ResponseEntity.ok(response);
    }
}