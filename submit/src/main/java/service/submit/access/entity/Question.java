package service.submit.access.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "question")
public class Question {
	@Id
    private String questionId;

    private String examId;

    private Object resource;       // nội dung câu hỏi linh hoạt
    private List<Answer> listAnswer;
}
