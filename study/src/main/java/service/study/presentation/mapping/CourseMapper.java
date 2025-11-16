package service.study.presentation.mapping;

import org.springframework.stereotype.Component;
import service.study.access.entity.Course;
import service.study.presentation.dto.CourseDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseMapper {
    
    public CourseDTO toDTO(Course course) {
        if (course == null) {
            return null;
        }
        
        return CourseDTO.builder()
                .courseId(course.getCourseId())
                .instructorId(course.getInstructorId())
                .title(course.getTitle())
                .description(course.getDescription())
                .category(course.getCategory())
                .price(course.getPrice())
                .level(course.getLevel())
                .status(course.getStatus())
                .image(course.getImage())
                .totalLesson(course.getTotalLesson())
                .totalDuration(course.getTotalDuration())
                .createdTime(course.getCreatedTime())
                .build();
    }
    
    public Course toEntity(CourseDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Course course = new Course();
        course.setCourseId(dto.getCourseId());
        course.setInstructorId(dto.getInstructorId());
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setCategory(dto.getCategory());
        course.setPrice(dto.getPrice());
        course.setLevel(dto.getLevel());
        course.setStatus(dto.getStatus());
        course.setImage(dto.getImage());
        course.setTotalLesson(dto.getTotalLesson());
        course.setTotalDuration(dto.getTotalDuration());
        course.setCreatedTime(dto.getCreatedTime());
        
        return course;
    }
    
    public List<CourseDTO> toDTOList(List<Course> courses) {
        if (courses == null) {
            return null;
        }
        
        return courses.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<Course> toEntityList(List<CourseDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}