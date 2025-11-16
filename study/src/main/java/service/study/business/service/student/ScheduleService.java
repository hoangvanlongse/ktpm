package service.study.business.service.student;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.study.access.entity.Course;
import service.study.access.entity.Student;
import service.study.access.entity.StudentCourse;
import service.study.access.repository.*;
import service.study.business.usecase.student.ScheduleUseCase;
import service.study.presentation.dto.CourseDTO;
import service.study.presentation.mapping.CourseMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ScheduleService implements ScheduleUseCase {
    
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final StudentCourseRepository studentCourseRepository;
    private final CourseMapper courseMapper;
    
    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> getAvailableCourses(String search, String category, Double minPrice, 
                                                Double maxPrice, String level, Long instructorId, 
                                                LocalDateTime createdTime, Pageable pageable) {
        log.info("Getting available courses with filters - search: {}, category: {}, minPrice: {}, maxPrice: {}, level: {}, instructorId: {}", 
                search, category, minPrice, maxPrice, level, instructorId);
        
        // Sử dụng repository method với JPQL query
        List<Course> courses = courseRepository.findAvailableCoursesWithFilters(
                search, category, minPrice, maxPrice, level, instructorId, pageable);
        
        // Convert to DTO
        return courseMapper.toDTOList(courses);
    }
    
    @Override
    public boolean enrollCourse(Long studentId, Long courseId) {
        log.info("Enrolling student {} to course {}", studentId, courseId);
        
        // Kiểm tra học sinh có tồn tại không
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        // Kiểm tra khóa học có tồn tại không
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        
        // Kiểm tra đã đăng ký chưa
        if (studentCourseRepository.existsByStudent_StudentIdAndCourse_CourseId(studentId, courseId)) {
            log.warn("Student {} already enrolled in course {}", studentId, courseId);
            return false;
        }
        
        // Tạo bản ghi đăng ký
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudent(student);
        studentCourse.setCourse(course);
        studentCourse.setProgress(0.0);
        studentCourse.setStatus("ENROLLED");
        studentCourse.setNumberCurrentLesson(0);
        
        studentCourseRepository.save(studentCourse);
        log.info("Successfully enrolled student {} to course {}", studentId, courseId);
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> getEnrolledCourses(Long studentId) {
        log.info("Getting enrolled courses for student {}", studentId);
        
        // Kiểm tra học sinh có tồn tại không
        studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        // Lấy danh sách student_course
        List<StudentCourse> studentCourses = studentCourseRepository.findByStudent_StudentId(studentId);
        
        // Eager load course để tránh LazyInitializationException
        studentCourses.forEach(sc -> {
            if (sc.getCourse() != null) {
                sc.getCourse().getTitle();
            }
        });
        
        // Extract courses và convert to DTO
        List<Course> courses = studentCourses.stream()
                .map(StudentCourse::getCourse)
                .collect(Collectors.toList());
        
        return courseMapper.toDTOList(courses);
    }
}