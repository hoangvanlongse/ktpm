package service.submit.access.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "exams")
public class Exam {
	@Id
    private String examId;

    private String referId; // courseID hoặc chapterID haowjc lessonID
    private String instructorId;

    private String title;
    private Object description;  // linh hoạt theo yêu cầu

    private Integer duration;
    private String beginTime;
    private String endTime;

    private String type;        // QUIZ / TEST / EXAM / ESSAY
    
    private String publicYn;    // "Y" hoặc "N"
    private String delYn;       // "Y" hoặc "N"

    private LocalDateTime createdTime;
}
