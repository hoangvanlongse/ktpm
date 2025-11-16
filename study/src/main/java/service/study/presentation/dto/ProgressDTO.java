package service.study.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressDTO {
    private Long courseId;
    private String courseTitle;
    private String courseImage;
    private Double progress; // percentage 0-100
    private String status;
    private Integer numberCurrentLesson;
    private Integer totalLesson;
    private LocalDateTime enrollTime;
}