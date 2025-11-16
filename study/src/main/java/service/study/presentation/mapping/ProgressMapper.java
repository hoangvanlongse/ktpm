package service.study.presentation.mapping;

import org.springframework.stereotype.Component;
import service.study.access.entity.Course;
import service.study.access.entity.StudentCourse;
import service.study.presentation.dto.ProgressDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProgressMapper {
    
    public ProgressDTO toDTO(StudentCourse studentCourse) {
        if (studentCourse == null) {
            return null;
        }
        
        Course course = studentCourse.getCourse();
        if (course == null) {
            return null;
        }
        
        return ProgressDTO.builder()
                .courseId(course.getCourseId())
                .courseTitle(course.getTitle())
                .courseImage(course.getImage())
                .progress(studentCourse.getProgress() != null ? studentCourse.getProgress() : 0.0)
                .status(studentCourse.getStatus())
                .numberCurrentLesson(studentCourse.getNumberCurrentLesson() != null ? studentCourse.getNumberCurrentLesson() : 0)
                .totalLesson(course.getTotalLesson() != null ? course.getTotalLesson() : 0)
                .enrollTime(studentCourse.getEnrollTime())
                .build();
    }
    
    public List<ProgressDTO> toDTOList(List<StudentCourse> studentCourses) {
        if (studentCourses == null) {
            return null;
        }
        
        return studentCourses.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}