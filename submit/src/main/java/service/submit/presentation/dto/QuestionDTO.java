package service.submit.presentation.dto;

import java.util.List;

import lombok.Data;

@Data
public class QuestionDTO {
	private String questionId;
    private String examId;

    private Object resource;
    private List<AnswerDTO> listAnswer;
}
