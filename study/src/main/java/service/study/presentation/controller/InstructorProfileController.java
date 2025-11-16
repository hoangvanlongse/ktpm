package service.study.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.study.access.entity.Instructor;
import service.study.business.usecase.intructor.InstructorProfileUseCase;
import service.study.presentation.dto.InstructorDTO;
import service.study.presentation.mapping.InstructorMapper;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/study/instructors")
@RequiredArgsConstructor
@Slf4j
public class InstructorProfileController {
    
    private final InstructorProfileUseCase instructorProfileUseCase;
    private final InstructorMapper instructorMapper;

    /**
     * API: Lấy thông tin người dạy theo ID
     * GET /api/study/instructors/{instructorId}
     */
    @GetMapping("/{instructorId}")
    public ResponseEntity<Map<String, Object>> getInstructor(@PathVariable Long instructorId) {
        log.info("Fetching instructor with id: {}", instructorId);
        try {
            Instructor instructor = instructorProfileUseCase.getById(instructorId);
            InstructorDTO instructorDTO = instructorMapper.toDTO(instructor);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Instructor fetched successfully");
            response.put("instructor", instructorDTO);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error fetching instructor: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
    
    /**
     * API: Tạo người dạy mới
     * POST /api/study/instructors
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createInstructor(@RequestBody InstructorDTO instructorDTO) {
        log.info("Creating new instructor: {}", instructorDTO.getEmail());
        
        try {
            Instructor instructor = instructorMapper.toEntity(instructorDTO);
            Long instructorId = instructorProfileUseCase.createInstructor(instructor);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Instructor created successfully");
            response.put("instructorId", instructorId);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            log.error("Error creating instructor: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    
    /**
     * API: Cập nhật người dạy
     * PUT /api/study/instructors/{instructorId}
     */
    @PutMapping("/{instructorId}")
    public ResponseEntity<Map<String, Object>> updateInstructor(
            @PathVariable Long instructorId,
            @RequestBody InstructorDTO instructorDTO) {
        log.info("Updating instructor with id: {}", instructorId);
        
        try {
            Instructor instructor = instructorMapper.toEntity(instructorDTO);
            instructor.setInstructorId(instructorId); // Đảm bảo instructorId được set
            Long updatedInstructorId = instructorProfileUseCase.updateInstructor(instructor);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Instructor updated successfully");
            response.put("instructorId", updatedInstructorId);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error updating instructor: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}

