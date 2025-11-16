package service.study.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.study.access.entity.Course;
import service.study.business.usecase.intructor.ManageCourseUseCase;
import service.study.presentation.dto.CourseDTO;
import service.study.presentation.mapping.CourseMapper;
import service.study.presentation.response.CourseListResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/study/courses")
@RequiredArgsConstructor
@Slf4j
public class ManageCourseController {
    
    private final ManageCourseUseCase manageCourseUseCase;
    private final CourseMapper courseMapper;
    
    /**
     * API: Tạo khóa học mới
     * POST /api/study/courses
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCourse(@RequestBody CourseDTO courseDTO) {
        log.info("Creating new course: {}", courseDTO.getTitle());
        
        Course course = courseMapper.toEntity(courseDTO);
        Long courseId = manageCourseUseCase.createCourse(course);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Course created successfully");
        response.put("courseId", courseId);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * API: Cập nhật khóa học
     * PUT /api/study/courses/{courseId}
     */
    @PutMapping("/{courseId}")
    public ResponseEntity<Map<String, Object>> updateCourse(
            @PathVariable Long courseId,
            @RequestBody CourseDTO courseDTO) {
        log.info("Updating course with id: {}", courseId);
        
        Course course = courseMapper.toEntity(courseDTO);
        course.setCourseId(courseId); // Đảm bảo courseId được set
        Long updatedCourseId = manageCourseUseCase.updateCourse(course);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Course updated successfully");
        response.put("courseId", updatedCourseId);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * API: Xóa khóa học
     * DELETE /api/study/courses/{courseId}
     */
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Map<String, Object>> deleteCourse(@PathVariable Long courseId) {
        log.info("Deleting course with id: {}", courseId);
        
        boolean success = manageCourseUseCase.deleteCourse(courseId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", success ? "Course deleted successfully" : "Course not found");
        response.put("success", success);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * API: Lấy khóa học theo ID
     * GET /api/study/courses/{courseId}
     */
    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long courseId) {
        log.info("Getting course by id: {}", courseId);
        
        Course course = manageCourseUseCase.getCourseById(courseId);
        CourseDTO courseDTO = courseMapper.toDTO(course);
        
        return ResponseEntity.ok(courseDTO);
    }
    
    /**
     * API: Lấy danh sách khóa học của người dạy (với filter)
     * GET /api/study/courses?instructorId=...&search=...&category=...&minPrice=...&maxPrice=...&level=...&status=...&createdTime=...&page=...&size=...
     */
    @GetMapping
    public ResponseEntity<CourseListResponse> getCourses(
            @RequestParam(defaultValue = "0") Long instructorId,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") String category,
            @RequestParam(defaultValue = "0.0") Double minPrice,
            @RequestParam(defaultValue = "0.0") Double maxPrice,
            @RequestParam(defaultValue = "") String level,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("Getting courses with filters - instructorId: {}, search: {}, category: {}, minPrice: {}, maxPrice: {}, level: {}, status: {}", 
                instructorId, search, category, minPrice, maxPrice, level, status);
        
        Pageable pageable = PageRequest.of(page, size);
        List<Course> courses = manageCourseUseCase.getCourses(
                instructorId, search, category, minPrice, maxPrice, level, status, createdTime, pageable);
        
        List<CourseDTO> courseDTOs = courseMapper.toDTOList(courses);
        CourseListResponse response = CourseListResponse.builder()
                .courses(courseDTOs)
                .total(courseDTOs.size())
                .build();
        
        return ResponseEntity.ok(response);
    }
}