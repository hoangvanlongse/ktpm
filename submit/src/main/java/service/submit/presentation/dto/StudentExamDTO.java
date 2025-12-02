package service.submit.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class StudentExamDTO {
	private String id;

    private String examId;
    private String studentId;

    private LocalDateTime submitTime;

    private Object submitResource;

    private List<StudentAnswerDTO> listResult;

    private Double grade;
    private Object feedback;
}
