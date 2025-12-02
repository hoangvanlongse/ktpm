package service.submit.access.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "student_exams")
public class StudentExam {
	@Id
    private String id;

    private String examId;
    private String studentId;

    private LocalDateTime submitTime;

    private Object submitResource;  // linh hoạt theo yêu cầu

    private List<StudentAnswer> listResult;

    private Double grade;
    private Object feedback;

	// init submit time when creating
	public StudentExam() {
		this.submitTime = LocalDateTime.now();
	}
}
