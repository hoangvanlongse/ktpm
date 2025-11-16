package service.study.business.service.instructor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.study.access.entity.Instructor;
import service.study.access.repository.InstructorRepository;
import service.study.business.usecase.intructor.InstructorProfileUseCase;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InstructorProfileService implements InstructorProfileUseCase {
    
    private final InstructorRepository instructorRepository;
    
    @Override
    public Instructor getById(Long instructorId) {
        log.info("Fetching instructor with id: {}", instructorId);
        return instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + instructorId));
    }
    
    @Override
    public Long createInstructor(Instructor instructor) {
        log.info("Creating new instructor: {}", instructor.getEmail());
        
        // Kiểm tra email đã tồn tại chưa
        if (instructor.getEmail() != null && instructorRepository.existsByEmail(instructor.getEmail())) {
            throw new RuntimeException("Email already exists: " + instructor.getEmail());
        }
        
        Instructor savedInstructor = instructorRepository.save(instructor);
        log.info("Successfully created instructor with id: {}", savedInstructor.getInstructorId());
        return savedInstructor.getInstructorId();
    }
    
    @SuppressWarnings("null")
    @Override
    public Long updateInstructor(Instructor instructor) {
        log.info("Updating instructor with id: {}", instructor.getInstructorId());
        
        // Kiểm tra instructor có tồn tại không
        Instructor existingInstructor = instructorRepository.findById(instructor.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + instructor.getInstructorId()));
        
        // Kiểm tra email có thay đổi và đã tồn tại chưa
        if (instructor.getEmail() != null && !instructor.getEmail().equals(existingInstructor.getEmail())) {
            if (instructorRepository.existsByEmail(instructor.getEmail())) {
                throw new RuntimeException("Email already exists: " + instructor.getEmail());
            }
        }
        
        // Cập nhật các field
        if (instructor.getName() != null) {
            existingInstructor.setName(instructor.getName());
        }
        if (instructor.getEmail() != null) {
            existingInstructor.setEmail(instructor.getEmail());
        }
        if (instructor.getAddress() != null) {
            existingInstructor.setAddress(instructor.getAddress());
        }
        if (instructor.getSex() != null) {
            existingInstructor.setSex(instructor.getSex());
        }
        if (instructor.getPhone() != null) {
            existingInstructor.setPhone(instructor.getPhone());
        }
        if (instructor.getDegree() != null) {
            existingInstructor.setDegree(instructor.getDegree());
        }
        if (instructor.getBirthday() != null) {
            existingInstructor.setBirthday(instructor.getBirthday());
        }
        
        Instructor updatedInstructor = instructorRepository.save(existingInstructor);
        log.info("Successfully updated instructor with id: {}", updatedInstructor.getInstructorId());
        return updatedInstructor.getInstructorId();
    }
}

