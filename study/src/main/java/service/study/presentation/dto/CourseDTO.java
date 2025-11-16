package service.study.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import service.study.access.entity.Instructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private Long courseId;
    private Long instructorId;
    private String title;
    private String description;
    private String category;
    private Double price;
    private String level;
    private String status;
    private String image;
    private Integer totalLesson;
    private Integer totalDuration;
    private LocalDateTime createdTime;
	
    private Instructor instructor;
}