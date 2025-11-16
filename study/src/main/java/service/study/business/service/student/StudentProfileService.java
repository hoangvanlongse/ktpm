package service.study.business.service.student;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.study.access.entity.Student;
import service.study.access.repository.StudentRepository;
import service.study.business.usecase.student.StudentProfileUseCase;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StudentProfileService implements StudentProfileUseCase {
    
    private final StudentRepository studentRepository;

    @Override
    public Student getById(Long studentId) {
        log.info("Fetching student with id: {}", studentId);
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
    }

    @Override
    public Long createStudent(Student student) {
        log.info("Creating new student: {}", student.getEmail());
        
        // Kiểm tra email đã tồn tại chưa
        if (student.getEmail() != null && studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Email already exists: " + student.getEmail());
        }
        
        Student savedStudent = studentRepository.save(student);
        log.info("Successfully created student with id: {}", savedStudent.getStudentId());
        return savedStudent.getStudentId();
    }
    
    @SuppressWarnings("null")
    @Override
    public Long updateStudent(Student student) {
        log.info("Updating student with id: {}", student.getStudentId());
        
        // Kiểm tra student có tồn tại không
        Student existingStudent = studentRepository.findById(student.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + student.getStudentId()));
        
        // Kiểm tra email có thay đổi và đã tồn tại chưa
        if (student.getEmail() != null && !student.getEmail().equals(existingStudent.getEmail())) {
            if (studentRepository.existsByEmail(student.getEmail())) {
                throw new RuntimeException("Email already exists: " + student.getEmail());
            }
        }
        
        // Cập nhật các field
        if (student.getName() != null) {
            existingStudent.setName(student.getName());
        }
        if (student.getEmail() != null) {
            existingStudent.setEmail(student.getEmail());
        }
        if (student.getAddress() != null) {
            existingStudent.setAddress(student.getAddress());
        }
        if (student.getSex() != null) {
            existingStudent.setSex(student.getSex());
        }
        if (student.getPhone() != null) {
            existingStudent.setPhone(student.getPhone());
        }
        if (student.getLevel() != null) {
            existingStudent.setLevel(student.getLevel());
        }
        if (student.getBirthday() != null) {
            existingStudent.setBirthday(student.getBirthday());
        }
        
        Student updatedStudent = studentRepository.save(existingStudent);
        log.info("Successfully updated student with id: {}", updatedStudent.getStudentId());
        return updatedStudent.getStudentId();
    }
}

