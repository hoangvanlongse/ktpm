package service.study.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.study.access.entity.Student;
import service.study.business.usecase.student.StudentProfileUseCase;
import service.study.presentation.dto.StudentDTO;
import service.study.presentation.mapping.StudentMapper;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/study/students")
@RequiredArgsConstructor
@Slf4j
public class StudentProfileController {
    
    private final StudentProfileUseCase studentProfileUseCase;
    private final StudentMapper studentMapper;

    /**
     * API: Tạo học sinh mới
     * GET /api/study/students/{studentId}
     */
    @GetMapping("/{studentId}")
    public ResponseEntity<Map<String, Object>> getStudent(@PathVariable Long studentId) {
        log.info("Fetching student with id: {}", studentId);
        
        try {
            Student student = studentProfileUseCase.getById(studentId);
            StudentDTO studentDTO = studentMapper.toDTO(student);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Student fetched successfully");
            response.put("student", studentDTO);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error fetching student: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        
    }
    
    /**
     * API: Tạo học sinh mới
     * POST /api/study/students
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createStudent(@RequestBody StudentDTO studentDTO) {
        log.info("Creating new student: {}", studentDTO.getEmail());
        
        try {
            Student student = studentMapper.toEntity(studentDTO);
            Long studentId = studentProfileUseCase.createStudent(student);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Student created successfully");
            response.put("studentId", studentId);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            log.error("Error creating student: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    
    /**
     * API: Cập nhật học sinh
     * PUT /api/study/students/{studentId}
     */
    @PutMapping("/{studentId}")
    public ResponseEntity<Map<String, Object>> updateStudent(
            @PathVariable Long studentId,
            @RequestBody StudentDTO studentDTO) {
        log.info("Updating student with id: {}", studentId);
        
        try {
            Student student = studentMapper.toEntity(studentDTO);
            student.setStudentId(studentId); // Đảm bảo studentId được set
            Long updatedStudentId = studentProfileUseCase.updateStudent(student);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Student updated successfully");
            response.put("studentId", updatedStudentId);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating student: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}

