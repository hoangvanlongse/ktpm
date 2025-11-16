package service.study.business.service.instructor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.study.access.entity.Course;
import service.study.access.repository.CourseRepository;
import service.study.business.usecase.intructor.ManageCourseUseCase;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ManageCourseService implements ManageCourseUseCase {
    
    private final CourseRepository courseRepository;
    
    @Override
    public Long createCourse(Course course) {
        log.info("Creating new course: {}", course.getTitle());
        
        // Set default values nếu cần
        if (course.getStatus() == null || course.getStatus().isEmpty()) {
            course.setStatus("DRAFT");
        }
        
        if (course.getCreatedTime() == null) {
            course.setCreatedTime(LocalDateTime.now());
        }
        
        Course savedCourse = courseRepository.save(course);
        log.info("Successfully created course with id: {}", savedCourse.getCourseId());
        return savedCourse.getCourseId();
    }
    
    @SuppressWarnings("null")
	@Override
    public Long updateCourse(Course course) {
        log.info("Updating course with id: {}", course.getCourseId());
        
        // Kiểm tra course có tồn tại không
        Course existingCourse = courseRepository.findById(course.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + course.getCourseId()));
        
        // Cập nhật các field
        if (course.getInstructorId() != null) {
            existingCourse.setInstructorId(course.getInstructorId());
        }
        if (course.getTitle() != null) {
            existingCourse.setTitle(course.getTitle());
        }
        if (course.getDescription() != null) {
            existingCourse.setDescription(course.getDescription());
        }
        if (course.getCategory() != null) {
            existingCourse.setCategory(course.getCategory());
        }
        if (course.getPrice() != null) {
            existingCourse.setPrice(course.getPrice());
        }
        if (course.getLevel() != null) {
            existingCourse.setLevel(course.getLevel());
        }
        if (course.getStatus() != null) {
            existingCourse.setStatus(course.getStatus());
        }
        if (course.getImage() != null) {
            existingCourse.setImage(course.getImage());
        }
        if (course.getTotalLesson() != null) {
            existingCourse.setTotalLesson(course.getTotalLesson());
        }
        if (course.getTotalDuration() != null) {
            existingCourse.setTotalDuration(course.getTotalDuration());
        }
        if (course.getDetail() != null) {
            existingCourse.setDetail(course.getDetail());
        }
        
        Course updatedCourse = courseRepository.save(existingCourse);
        log.info("Successfully updated course with id: {}", updatedCourse.getCourseId());
        return updatedCourse.getCourseId();
    }
    
    @Override
    public boolean deleteCourse(Long courseId) {
        log.info("Deleting course with id: {}", courseId);
        
        if (!courseRepository.existsById(courseId)) {
            log.warn("Course with id {} does not exist, considering as success", courseId);
            return true;
        }
        
        courseRepository.deleteById(courseId);
        log.info("Successfully deleted course with id: {}", courseId);
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Course getCourseById(Long courseId) {
        log.info("Getting course by id: {}", courseId);
        
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Course> getCourses(Long instructorId, String search, String category,
            Double minPrice, Double maxPrice, String level, String status, LocalDateTime createdTime, Pageable pageable) {
        log.info("Getting courses for instructor {} with filters - search: {}, category: {}, minPrice: {}, maxPrice: {}, level: {}, status: {}", 
                instructorId, search, category, minPrice, maxPrice, level, status);
        
        // Sử dụng repository method với JPQL query
        return courseRepository.findByInstructorWithFilters(
                instructorId, search, category, minPrice, maxPrice, level, status, pageable);
    }
}