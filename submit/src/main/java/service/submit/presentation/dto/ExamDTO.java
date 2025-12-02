package service.submit.presentation.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ExamDTO {
	private String examId;
    private String referId;
    private String instructorId;

    private String title;
    private Object description;

    private Integer duration;
    private String beginTime;
    private String endTime;

    private String type;
    private String publicYn; 
    private String delYn;

    private LocalDateTime createdTime;
}
